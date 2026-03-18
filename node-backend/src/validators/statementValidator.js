/**
 * Statement Validator Module
 *
 * Validates monthly client statement data against comprehensive business rules:
 * - Transaction completeness and consistency
 * - Data type correctness
 * - Value range validation
 * - Missing data detection
 * - Format compliance
 *
 * @module validators/statementValidator
 */

import Joi from 'joi';

/**
 * Joi schema for individual transactions
 * Ensures each transaction has valid structure and values
 */
const transactionSchema = Joi.object({
  id: Joi.string().required().messages({
    'any.required': 'Transaction ID is required',
    'string.empty': 'Transaction ID cannot be empty'
  }),
  date: Joi.date().required().messages({
    'any.required': 'Transaction date is required',
    'date.base': 'Transaction date must be a valid date'
  }),
  description: Joi.string().min(3).max(255).required().messages({
    'any.required': 'Transaction description is required',
    'string.min': 'Description must be at least 3 characters',
    'string.max': 'Description cannot exceed 255 characters'
  }),
  amount: Joi.number().required().messages({
    'any.required': 'Transaction amount is required',
    'number.base': 'Amount must be a number'
  }),
  type: Joi.string().valid('debit', 'credit').required().messages({
    'any.required': 'Transaction type is required',
    'any.only': 'Transaction type must be either "debit" or "credit"'
  }),
  category: Joi.string().min(2).max(50).required().messages({
    'any.required': 'Transaction category is required'
  }),
  status: Joi.string().valid('pending', 'completed', 'cancelled').default('completed').messages({
    'any.only': 'Status must be pending, completed, or cancelled'
  }),
  reference: Joi.string().optional()
});

/**
 * Joi schema for client information
 */
const clientSchema = Joi.object({
  id: Joi.string().required().messages({
    'any.required': 'Client ID is required'
  }),
  name: Joi.string().min(2).max(100).required().messages({
    'any.required': 'Client name is required',
    'string.min': 'Name must be at least 2 characters'
  }),
  email: Joi.string().email().required().messages({
    'any.required': 'Client email is required',
    'string.email': 'Email must be valid'
  }),
  accountNumber: Joi.string().required().messages({
    'any.required': 'Account number is required'
  })
});

/**
 * Joi schema for the complete monthly statement
 */
const statementSchema = Joi.object({
  client: clientSchema.required(),
  month: Joi.string()
    .pattern(/^\d{4}-\d{2}$/)
    .required()
    .messages({
      'any.required': 'Month is required',
      'string.pattern.base': 'Month must be in YYYY-MM format'
    }),
  year: Joi.number().integer().min(2000).max(2099).required().messages({
    'any.required': 'Year is required',
    'number.integer': 'Year must be an integer',
    'number.min': 'Year must be 2000 or later'
  }),
  openingBalance: Joi.number().required().messages({
    'any.required': 'Opening balance is required'
  }),
  closingBalance: Joi.number().required().messages({
    'any.required': 'Closing balance is required'
  }),
  transactions: Joi.array().items(transactionSchema).min(1).required().messages({
    'any.required': 'Transactions array is required',
    'array.min': 'At least one transaction is required'
  }),
  currency: Joi.string().length(3).default('USD').messages({
    'string.length': 'Currency must be a 3-letter ISO code'
  })
});

/**
 * Validates a monthly statement against comprehensive business rules
 *
 * @param {Object} statement - The statement object to validate
 * @returns {Object} Validation result { isValid: boolean, errors: array, warnings: array }
 */
export function validateStatement(statement) {
  const errors = [];
  const warnings = [];

  // Joi schema validation
  const { error, value } = statementSchema.validate(statement, {
    abortEarly: false,
    stripUnknown: false
  });

  if (error) {
    error.details.forEach(detail => {
      errors.push({
        path: detail.path.join('.'),
        message: detail.message,
        type: 'schema'
      });
    });
  }

  if (errors.length > 0) {
    return { isValid: false, errors, warnings };
  }

  // Custom business rule validations
  const balanceCheckResult = validateBalance(value);
  if (!balanceCheckResult.isValid) {
    errors.push(...balanceCheckResult.errors);
  }
  warnings.push(...balanceCheckResult.warnings);

  // Validate transaction dates
  const dateCheckResult = validateTransactionDates(value);
  if (!dateCheckResult.isValid) {
    errors.push(...dateCheckResult.errors);
  }
  warnings.push(...dateCheckResult.warnings);

  // Validate amount consistency
  const amountCheckResult = validateAmounts(value);
  if (!amountCheckResult.isValid) {
    errors.push(...amountCheckResult.errors);
  }
  warnings.push(...amountCheckResult.warnings);

  return {
    isValid: errors.length === 0,
    errors,
    warnings,
    validatedStatement: value
  };
}

/**
 * Validates opening/closing balance consistency with transactions
 *
 * @private
 * @param {Object} statement - The validated statement
 * @returns {Object} Validation result
 */
function validateBalance(statement) {
  const errors = [];
  const warnings = [];

  const { openingBalance, closingBalance, transactions } = statement;

  // Calculate expected closing balance
  let calculatedClosing = openingBalance;
  transactions.forEach(tx => {
    if (tx.type === 'credit') {
      calculatedClosing += Math.abs(tx.amount);
    } else if (tx.type === 'debit') {
      calculatedClosing -= Math.abs(tx.amount);
    }
  });

  // Allow for small floating point differences (0.01)
  const difference = Math.abs(calculatedClosing - closingBalance);
  if (difference > 0.01) {
    errors.push({
      path: 'balanceReconciliation',
      message: `Closing balance mismatch. Expected: ${calculatedClosing.toFixed(2)}, Actual: ${closingBalance.toFixed(2)}`,
      type: 'balance'
    });
  }

  return { isValid: errors.length === 0, errors, warnings };
}

/**
 * Validates transaction dates are within statement month
 *
 * @private
 * @param {Object} statement - The validated statement
 * @returns {Object} Validation result
 */
function validateTransactionDates(statement) {
  const errors = [];
  const warnings = [];

  const { month, year, transactions } = statement;
  const [statementYear, statementMonth] = month.split('-');

  transactions.forEach((tx, index) => {
    const txDate = new Date(tx.date);
    const txYear = txDate.getFullYear();
    const txMonth = String(txDate.getMonth() + 1).padStart(2, '0');

    if (txYear !== parseInt(year) || txMonth !== statementMonth) {
      warnings.push({
        path: `transactions[${index}].date`,
        message: `Transaction date ${tx.date} is outside statement period ${month}`,
        type: 'dateWarning'
      });
    }
  });

  return { isValid: true, errors, warnings };
}

/**
 * Validates amount consistency and reasonableness
 *
 * @private
 * @param {Object} statement - The validated statement
 * @returns {Object} Validation result
 */
function validateAmounts(statement) {
  const errors = [];
  const warnings = [];

  const { transactions } = statement;
  let totalDebits = 0;
  let totalCredits = 0;

  transactions.forEach((tx, index) => {
    // Check for zero amounts
    if (tx.amount === 0) {
      warnings.push({
        path: `transactions[${index}].amount`,
        message: `Transaction ${tx.id} has zero amount`,
        type: 'zeroAmount'
      });
    }

    // Check for negative amounts (should use type instead)
    if (tx.amount < 0) {
      warnings.push({
        path: `transactions[${index}].amount`,
        message: `Transaction ${tx.id} has negative amount. Consider using type field instead`,
        type: 'negativeAmount'
      });
    }

    // Accumulate totals
    if (tx.type === 'debit') {
      totalDebits += Math.abs(tx.amount);
    } else {
      totalCredits += Math.abs(tx.amount);
    }
  });

  return { isValid: errors.length === 0, errors, warnings };
}

/**
 * Validates individual transaction
 *
 * @param {Object} transaction - Single transaction to validate
 * @returns {Object} Validation result
 */
export function validateTransaction(transaction) {
  const { error, value } = transactionSchema.validate(transaction, {
    abortEarly: false
  });

  return {
    isValid: !error,
    errors: error ? error.details : [],
    transaction: value
  };
}

/**
 * Batch validation for multiple statements
 *
 * @param {Array} statements - Array of statements to validate
 * @returns {Array} Array of validation results
 */
export function validateStatementBatch(statements) {
  if (!Array.isArray(statements)) {
    return [{
      isValid: false,
      errors: [{ message: 'Input must be an array of statements', type: 'input' }]
    }];
  }

  return statements.map((statement, index) => ({
    index,
    ...validateStatement(statement)
  }));
}

export default {
  validateStatement,
  validateTransaction,
  validateStatementBatch
};


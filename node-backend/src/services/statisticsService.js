/**
 * Statement Statistics Module
 *
 * Computes comprehensive financial summary statistics from validated statements:
 * - Transaction summaries (totals, counts)
 * - Category-based analysis
 * - Trend analysis
 * - Statistical measures (mean, median, std dev)
 * - Ratio calculations
 *
 * @module services/statisticsService
 */

/**
 * Computes comprehensive statistics for a validated statement
 *
 * @param {Object} statement - The validated statement object
 * @returns {Object} Comprehensive statistics object
 */
export function computeStatistics(statement) {
  const {
    transactions,
    openingBalance,
    closingBalance,
    month,
    currency
  } = statement;

  return {
    period: month,
    currency,
    balanceMetrics: computeBalanceMetrics(
      openingBalance,
      closingBalance,
      transactions
    ),
    transactionMetrics: computeTransactionMetrics(transactions),
    categoryAnalysis: analyzeCategoryDistribution(transactions),
    trends: analyzeTrends(transactions),
    riskIndicators: computeRiskIndicators(statement)
  };
}

/**
 * Computes balance-related metrics
 *
 * @private
 * @param {number} openingBalance - Opening balance
 * @param {number} closingBalance - Closing balance
 * @param {Array} transactions - Array of transactions
 * @returns {Object} Balance metrics
 */
function computeBalanceMetrics(openingBalance, closingBalance, transactions) {
  const totalDebits = transactions
    .filter(tx => tx.type === 'debit')
    .reduce((sum, tx) => sum + Math.abs(tx.amount), 0);

  const totalCredits = transactions
    .filter(tx => tx.type === 'credit')
    .reduce((sum, tx) => sum + Math.abs(tx.amount), 0);

  const netFlow = totalCredits - totalDebits;
  const balanceChange = closingBalance - openingBalance;

  return {
    opening: parseFloat(openingBalance.toFixed(2)),
    closing: parseFloat(closingBalance.toFixed(2)),
    change: parseFloat(balanceChange.toFixed(2)),
    percentageChange: parseFloat(
      (((closingBalance - openingBalance) / Math.abs(openingBalance)) * 100).toFixed(2)
    ),
    totalInflow: parseFloat(totalCredits.toFixed(2)),
    totalOutflow: parseFloat(totalDebits.toFixed(2)),
    netFlow: parseFloat(netFlow.toFixed(2)),
    averageDailyBalance: computeAverageDailyBalance(
      openingBalance,
      closingBalance,
      transactions
    )
  };
}

/**
 * Computes average daily balance throughout the month
 *
 * @private
 * @param {number} openingBalance - Opening balance
 * @param {number} closingBalance - Closing balance
 * @param {Array} transactions - Array of transactions
 * @returns {number} Average daily balance
 */
function computeAverageDailyBalance(openingBalance, closingBalance, transactions) {
  // Simplified calculation: average of opening and closing
  // In production, would track daily balances through all transactions
  const average = (openingBalance + closingBalance) / 2;
  return parseFloat(average.toFixed(2));
}

/**
 * Computes transaction-level metrics
 *
 * @private
 * @param {Array} transactions - Array of transactions
 * @returns {Object} Transaction metrics
 */
function computeTransactionMetrics(transactions) {
  const debits = transactions
    .filter(tx => tx.type === 'debit')
    .map(tx => Math.abs(tx.amount))
    .sort((a, b) => a - b);

  const credits = transactions
    .filter(tx => tx.type === 'credit')
    .map(tx => Math.abs(tx.amount))
    .sort((a, b) => a - b);

  const allAmounts = transactions.map(tx => Math.abs(tx.amount)).sort((a, b) => a - b);

  return {
    totalTransactions: transactions.length,
    completedTransactions: transactions.filter(tx => tx.status === 'completed').length,
    pendingTransactions: transactions.filter(tx => tx.status === 'pending').length,
    cancelledTransactions: transactions.filter(tx => tx.status === 'cancelled').length,
    debitMetrics: computeAmountMetrics(debits, 'Debit'),
    creditMetrics: computeAmountMetrics(credits, 'Credit'),
    overallMetrics: computeAmountMetrics(allAmounts, 'Overall')
  };
}

/**
 * Computes statistical metrics for a set of amounts
 *
 * @private
 * @param {Array} amounts - Array of transaction amounts
 * @param {string} label - Label for the metric set
 * @returns {Object} Statistical metrics
 */
function computeAmountMetrics(amounts, label) {
  if (amounts.length === 0) {
    return {
      label,
      count: 0,
      total: 0,
      average: 0,
      median: 0,
      min: 0,
      max: 0,
      standardDeviation: 0
    };
  }

  const total = amounts.reduce((sum, val) => sum + val, 0);
  const average = total / amounts.length;
  const median = computeMedian(amounts);

  const variance = amounts.reduce((sum, val) => sum + Math.pow(val - average, 2), 0) / amounts.length;
  const standardDeviation = Math.sqrt(variance);

  return {
    label,
    count: amounts.length,
    total: parseFloat(total.toFixed(2)),
    average: parseFloat(average.toFixed(2)),
    median: parseFloat(median.toFixed(2)),
    min: parseFloat(amounts[0].toFixed(2)),
    max: parseFloat(amounts[amounts.length - 1].toFixed(2)),
    standardDeviation: parseFloat(standardDeviation.toFixed(2))
  };
}

/**
 * Computes median of an array (assumes sorted array)
 *
 * @private
 * @param {Array} sortedArray - Sorted array of numbers
 * @returns {number} Median value
 */
function computeMedian(sortedArray) {
  const mid = Math.floor(sortedArray.length / 2);
  if (sortedArray.length % 2 !== 0) {
    return sortedArray[mid];
  }
  return (sortedArray[mid - 1] + sortedArray[mid]) / 2;
}

/**
 * Analyzes transaction distribution by category
 *
 * @private
 * @param {Array} transactions - Array of transactions
 * @returns {Object} Category distribution analysis
 */
function analyzeCategoryDistribution(transactions) {
  const categoryMap = new Map();

  transactions.forEach(tx => {
    if (!categoryMap.has(tx.category)) {
      categoryMap.set(tx.category, {
        name: tx.category,
        count: 0,
        totalDebit: 0,
        totalCredit: 0,
        transactions: []
      });
    }

    const cat = categoryMap.get(tx.category);
    cat.count++;
    cat.transactions.push({
      id: tx.id,
      amount: tx.amount,
      type: tx.type,
      date: tx.date
    });

    if (tx.type === 'debit') {
      cat.totalDebit += Math.abs(tx.amount);
    } else {
      cat.totalCredit += Math.abs(tx.amount);
    }
  });

  const categories = Array.from(categoryMap.values()).map(cat => ({
    name: cat.name,
    transactionCount: cat.count,
    percentageOfTotal: parseFloat(
      ((cat.count / transactions.length) * 100).toFixed(2)
    ),
    totalDebit: parseFloat(cat.totalDebit.toFixed(2)),
    totalCredit: parseFloat(cat.totalCredit.toFixed(2)),
    netAmount: parseFloat((cat.totalCredit - cat.totalDebit).toFixed(2)),
    topTransactions: cat.transactions
      .sort((a, b) => Math.abs(b.amount) - Math.abs(a.amount))
      .slice(0, 3)
  }));

  return {
    totalCategories: categories.length,
    categories: categories.sort((a, b) => b.transactionCount - a.transactionCount)
  };
}

/**
 * Analyzes transaction trends
 *
 * @private
 * @param {Array} transactions - Array of transactions
 * @returns {Object} Trend analysis
 */
function analyzeTrends(transactions) {
  // Group transactions by week and day
  const weeklyData = groupByWeek(transactions);
  const dailyData = groupByDay(transactions);

  // Find peak transaction day
  const peakDay = dailyData.reduce((max, current) =>
    current.amount > max.amount ? current : max
  );

  // Find lowest transaction day
  const lowestDay = dailyData.reduce((min, current) =>
    current.amount < min.amount ? current : min
  );

  return {
    weeklyDistribution: weeklyData,
    dailyDistribution: dailyData,
    peakDay: {
      date: peakDay.date,
      transactionCount: peakDay.count,
      totalAmount: peakDay.amount
    },
    lowestDay: {
      date: lowestDay.date,
      transactionCount: lowestDay.count,
      totalAmount: lowestDay.amount
    },
    averageTransactionsPerDay: parseFloat(
      (transactions.length / 31).toFixed(2)
    )
  };
}

/**
 * Groups transactions by week
 *
 * @private
 * @param {Array} transactions - Array of transactions
 * @returns {Array} Weekly grouped data
 */
function groupByWeek(transactions) {
  const weekMap = new Map();

  transactions.forEach(tx => {
    const date = new Date(tx.date);
    const weekStart = new Date(date);
    weekStart.setDate(weekStart.getDate() - weekStart.getDay());
    const weekKey = weekStart.toISOString().split('T')[0];

    if (!weekMap.has(weekKey)) {
      weekMap.set(weekKey, { week: weekKey, count: 0, amount: 0 });
    }

    const week = weekMap.get(weekKey);
    week.count++;
    week.amount += Math.abs(tx.amount);
  });

  return Array.from(weekMap.values())
    .map(w => ({
      week: w.week,
      transactionCount: w.count,
      totalAmount: parseFloat(w.amount.toFixed(2))
    }))
    .sort((a, b) => a.week.localeCompare(b.week));
}

/**
 * Groups transactions by day
 *
 * @private
 * @param {Array} transactions - Array of transactions
 * @returns {Array} Daily grouped data
 */
function groupByDay(transactions) {
  const dayMap = new Map();

  transactions.forEach(tx => {
    const date = new Date(tx.date);
    const dayKey = date.toISOString().split('T')[0];

    if (!dayMap.has(dayKey)) {
      dayMap.set(dayKey, { date: dayKey, count: 0, amount: 0 });
    }

    const day = dayMap.get(dayKey);
    day.count++;
    day.amount += Math.abs(tx.amount);
  });

  return Array.from(dayMap.values())
    .map(d => ({
      date: d.date,
      transactionCount: d.count,
      totalAmount: parseFloat(d.amount.toFixed(2))
    }))
    .sort((a, b) => a.date.localeCompare(b.date));
}

/**
 * Computes risk indicators for potential issues
 *
 * @private
 * @param {Object} statement - The statement object
 * @returns {Object} Risk indicators
 */
function computeRiskIndicators(statement) {
  const { transactions, closingBalance, openingBalance } = statement;
  const indicators = [];
  const riskScore = { low: 0, medium: 0, high: 0 };

  // Check for unusual balance fluctuations
  if (Math.abs(closingBalance - openingBalance) > Math.abs(openingBalance) * 0.5) {
    indicators.push({
      type: 'largeBalanceChange',
      severity: 'medium',
      message: 'Significant balance change detected (>50%)',
      value: ((Math.abs(closingBalance - openingBalance) / Math.abs(openingBalance)) * 100).toFixed(2) + '%'
    });
    riskScore.medium++;
  }

  // Check for pending transactions
  const pending = transactions.filter(tx => tx.status === 'pending');
  if (pending.length > 0) {
    indicators.push({
      type: 'pendingTransactions',
      severity: 'low',
      message: `${pending.length} pending transactions need review`,
      value: pending.length
    });
    riskScore.low++;
  }

  // Check for large individual transactions
  const largeTransactions = transactions.filter(tx => Math.abs(tx.amount) > Math.abs(openingBalance) * 0.1);
  if (largeTransactions.length > 0) {
    indicators.push({
      type: 'largeTransactions',
      severity: 'medium',
      message: `${largeTransactions.length} large transactions (>10% of opening balance)`,
      value: largeTransactions.length
    });
    riskScore.medium++;
  }

  // Check for unusual transaction patterns (many small transactions)
  const smallTransactions = transactions.filter(tx => Math.abs(tx.amount) < Math.abs(openingBalance) * 0.01);
  if (smallTransactions.length > transactions.length * 0.5) {
    indicators.push({
      type: 'unusualPattern',
      severity: 'low',
      message: 'High number of small transactions detected',
      value: smallTransactions.length
    });
    riskScore.low++;
  }

  // Check for cancelled transactions
  const cancelled = transactions.filter(tx => tx.status === 'cancelled');
  if (cancelled.length > transactions.length * 0.1) {
    indicators.push({
      type: 'highCancellation',
      severity: 'medium',
      message: `${cancelled.length} cancelled transactions (>10% of total)`,
      value: cancelled.length
    });
    riskScore.medium++;
  }

  return {
    overallRiskLevel: riskScore.high > 0 ? 'high' : riskScore.medium > 0 ? 'medium' : 'low',
    indicators,
    summary: {
      lowRiskItems: riskScore.low,
      mediumRiskItems: riskScore.medium,
      highRiskItems: riskScore.high
    }
  };
}

export default {
  computeStatistics
};


/**
 * Statement Analysis Service
 * 
 * Main orchestrator service that:
 * 1. Validates incoming statement data
 * 2. Computes summary statistics
 * 3. Generates AI-driven client insights
 * 4. Handles error cases gracefully
 * 
 * @module services/statementAnalysisService
 */
import { validateStatement } from '../validators/statementValidator.js';
import { computeStatistics } from './statisticsService.js';
import { generateClientInsights } from './aiInsightsService.js';
import logger from '../utils/logger.js';
/**
 * Analyzes a complete statement in three stages:
 * 1. Validation - Ensures data integrity
 * 2. Statistics - Computes metrics
 * 3. Insights - Generates AI narratives
 * 
 * @param {Object} statement - Raw statement data from client
 * @returns {Promise<Object>} Complete analysis result
 */
export async function analyzeStatement(statement) {
  const analysisStartTime = Date.now();
  const analysisId = generateAnalysisId();
  try {
    logger.info(`[${analysisId}] Starting statement analysis for client: ${statement.client?.id}`);
    // Stage 1: Validation
    logger.debug(`[${analysisId}] Stage 1: Validating statement structure and data integrity`);
    const validationResult = validateStatement(statement);
    if (!validationResult.isValid) {
      logger.warn(`[${analysisId}] Validation failed with ${validationResult.errors.length} errors`);
      return {
        analysisId,
        status: 'validation_failed',
        errors: validationResult.errors,
        warnings: validationResult.warnings,
        timestamp: new Date().toISOString(),
        durationMs: Date.now() - analysisStartTime
      };
    }
    if (validationResult.warnings.length > 0) {
      logger.info(`[${analysisId}] Validation warnings: ${validationResult.warnings.length}`);
    }
    const validatedStatement = validationResult.validatedStatement;
    // Stage 2: Statistics Computation
    logger.debug(`[${analysisId}] Stage 2: Computing statistical summaries`);
    const statistics = computeStatistics(validatedStatement);
    logger.debug(`[${analysisId}] Computed metrics: ${statistics.transactionMetrics.totalTransactions} transactions analyzed`);
    // Stage 3: AI Insight Generation
    logger.debug(`[${analysisId}] Stage 3: Generating AI-driven client insights`);
    const insights = await generateClientInsights(
      validatedStatement.client,
      statistics,
      validatedStatement.month
    );
    const durationMs = Date.now() - analysisStartTime;
    logger.info(`[${analysisId}] Analysis completed successfully in ${durationMs}ms`);
    return {
      analysisId,
      status: 'success',
      timestamp: new Date().toISOString(),
      durationMs,
      validation: {
        isValid: true,
        warningCount: validationResult.warnings.length,
        warnings: validationResult.warnings
      },
      statistics: {
        period: statistics.period,
        currency: statistics.currency,
        balanceMetrics: statistics.balanceMetrics,
        transactionMetrics: statistics.transactionMetrics,
        categoryAnalysis: statistics.categoryAnalysis,
        trends: statistics.trends,
        riskIndicators: statistics.riskIndicators
      },
      insights: {
        narrative: insights.narrativeInsight,
        source: insights.source,
        structured: insights.structuredInsights,
        recommendations: insights.recommendations
      },
      client: {
        id: validatedStatement.client.id,
        name: validatedStatement.client.name,
        email: validatedStatement.client.email
      }
    };
  } catch (error) {
    logger.error(`[${analysisId}] Analysis failed with error: ${error.message}`, error);
    return {
      analysisId,
      status: 'error',
      error: {
        message: error.message,
        type: error.constructor.name
      },
      timestamp: new Date().toISOString(),
      durationMs: Date.now() - analysisStartTime
    };
  }
}
/**
 * Batch analyzes multiple statements
 * 
 * @param {Array} statements - Array of statement objects
 * @param {Object} options - Options { concurrent: number }
 * @returns {Promise<Array>} Array of analysis results
 */
export async function analyzeStatementBatch(statements, options = {}) {
  const { concurrent = 3 } = options;
  const batchId = generateAnalysisId();
  const batchStartTime = Date.now();
  if (!Array.isArray(statements)) {
    logger.error(`[${batchId}] Invalid input: expected array of statements`);
    return [{
      status: 'error',
      error: 'Input must be an array of statements'
    }];
  }
  logger.info(`[${batchId}] Starting batch analysis of ${statements.length} statements (concurrency: ${concurrent})`);
  const results = [];
  const queue = [...statements];
  const processing = [];
  while (queue.length > 0 || processing.length > 0) {
    // Add new items up to concurrent limit
    while (processing.length < concurrent && queue.length > 0) {
      const statement = queue.shift();
      const promise = analyzeStatement(statement)
        .then(result => {
          results.push(result);
          processing.splice(processing.indexOf(promise), 1);
        })
        .catch(error => {
          logger.error(`[${batchId}] Error in batch item: ${error.message}`);
          results.push({
            status: 'error',
            error: error.message
          });
          processing.splice(processing.indexOf(promise), 1);
        });
      processing.push(promise);
    }
    // Wait for at least one to complete if queue has items or processing has items
    if (processing.length > 0) {
      await Promise.race(processing);
    }
  }
  const batchDurationMs = Date.now() - batchStartTime;
  const successCount = results.filter(r => r.status === 'success').length;
  logger.info(`[${batchId}] Batch analysis completed: ${successCount}/${statements.length} successful in ${batchDurationMs}ms`);
  return {
    batchId,
    status: 'completed',
    timestamp: new Date().toISOString(),
    durationMs: batchDurationMs,
    summary: {
      total: statements.length,
      successful: successCount,
      failed: statements.length - successCount,
      successRate: parseFloat(((successCount / statements.length) * 100).toFixed(2)) + '%'
    },
    results
  };
}
/**
 * Generates a unique analysis ID for tracking
 * 
 * @private
 * @returns {string} Unique analysis ID
 */
function generateAnalysisId() {
  return `ANALYSIS-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
}
export default {
  analyzeStatement,
  analyzeStatementBatch
};

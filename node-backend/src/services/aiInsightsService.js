/**
 * AI Insights Service Module
 *
 * Generates client-facing insights using generative AI (OpenAI/Claude):
 * - Executive summary of key metrics
 * - Personalized financial recommendations
 * - Trend analysis narratives
 * - Risk warnings and opportunities
 * - Natural language insights
 *
 * @module services/aiInsightsService
 */

import { OpenAI } from 'openai';
import logger from '../utils/logger.js';

/**
 * Initialize OpenAI client
 * Falls back to mock service if API key not configured
 */
const initializeOpenAIClient = () => {
  const apiKey = process.env.OPENAI_API_KEY;
  if (!apiKey) {
    logger.warn('OPENAI_API_KEY not configured. Using mock AI service.');
    return null;
  }
  return new OpenAI({ apiKey });
};

let openaiClient = initializeOpenAIClient();

/**
 * Generates comprehensive AI-driven client insights from statement statistics
 *
 * @param {Object} client - Client information { id, name, email }
 * @param {Object} statistics - Computed statistics from statementValidator
 * @param {string} month - Statement month (YYYY-MM format)
 * @returns {Promise<Object>} AI-generated insights
 */
export async function generateClientInsights(client, statistics, month) {
  try {
    const prompt = buildInsightPrompt(client, statistics, month);

    if (!openaiClient) {
      logger.info('Using mock AI service for insights generation');
      return generateMockInsights(client, statistics, month);
    }

    const response = await openaiClient.chat.completions.create({
      model: 'gpt-4o-mini',
      messages: [
        {
          role: 'system',
          content: `You are a professional financial analyst creating concise, client-friendly insights from banking statements.
                   Be conversational but professional. Highlight key metrics, trends, and actionable recommendations.
                   Keep insights focused, clear, and valuable for the client.`
        },
        {
          role: 'user',
          content: prompt
        }
      ],
      temperature: 0.7,
      max_tokens: 1000
    });

    const insightText = response.choices[0]?.message?.content || '';

    return {
      status: 'success',
      source: 'openai',
      timestamp: new Date().toISOString(),
      clientId: client.id,
      month,
      narrativeInsight: insightText,
      structuredInsights: extractStructuredInsights(statistics),
      recommendations: generateRecommendations(statistics)
    };
  } catch (error) {
    logger.error('Error generating AI insights:', error);

    // Fallback to mock service on error
    return generateMockInsights(client, statistics, month);
  }
}

/**
 * Builds the prompt for AI insight generation
 *
 * @private
 * @param {Object} client - Client information
 * @param {Object} statistics - Computed statistics
 * @param {string} month - Statement month
 * @returns {string} Formatted prompt
 */
function buildInsightPrompt(client, statistics, month) {
  const {
    balanceMetrics,
    transactionMetrics,
    categoryAnalysis,
    trends,
    riskIndicators
  } = statistics;

  return `
Please analyze the following banking statement for ${client.name} for ${month} and provide valuable, actionable insights.

CLIENT: ${client.name} (Account: ${client.email})
MONTH: ${month}

BALANCE SUMMARY:
- Opening Balance: ${balanceMetrics.opening}
- Closing Balance: ${balanceMetrics.closing}
- Change: ${balanceMetrics.change} (${balanceMetrics.percentageChange}%)
- Total Inflow: ${balanceMetrics.totalInflow}
- Total Outflow: ${balanceMetrics.totalOutflow}
- Net Flow: ${balanceMetrics.netFlow}

TRANSACTION OVERVIEW:
- Total Transactions: ${transactionMetrics.totalTransactions}
- Completed: ${transactionMetrics.completedTransactions}
- Pending: ${transactionMetrics.pendingTransactions}
- Cancelled: ${transactionMetrics.cancelledTransactions}

TRANSACTION STATISTICS:
Debits - Average: ${transactionMetrics.debitMetrics.average}, Min: ${transactionMetrics.debitMetrics.min}, Max: ${transactionMetrics.debitMetrics.max}
Credits - Average: ${transactionMetrics.creditMetrics.average}, Min: ${transactionMetrics.creditMetrics.min}, Max: ${transactionMetrics.creditMetrics.max}

TOP CATEGORIES:
${categoryAnalysis.categories.slice(0, 5)
  .map(cat => `- ${cat.name}: ${cat.transactionCount} transactions, Net: ${cat.netAmount}`)
  .join('\n')}

TRENDS:
- Peak Day: ${trends.peakDay.date} (${trends.peakDay.transactionCount} transactions)
- Average Daily Transactions: ${trends.averageTransactionsPerDay}

RISK ASSESSMENT:
- Overall Risk Level: ${riskIndicators.overallRiskLevel}
- Alerts: ${riskIndicators.indicators.map(i => i.message).join('; ') || 'None'}

Please provide:
1. A brief executive summary (2-3 sentences)
2. Key insights about spending patterns and trends
3. Any concerning patterns or opportunities
4. 2-3 specific, actionable recommendations for the client

Keep the tone professional yet conversational and client-friendly.
  `.trim();
}

/**
 * Generates mock AI insights when API is unavailable
 * Used for testing and fallback scenarios
 *
 * @private
 * @param {Object} client - Client information
 * @param {Object} statistics - Computed statistics
 * @param {string} month - Statement month
 * @returns {Object} Mock AI insights
 */
function generateMockInsights(client, statistics, month) {
  const { balanceMetrics, transactionMetrics, categoryAnalysis, riskIndicators } = statistics;

  const balanceDirection = balanceMetrics.change > 0 ? 'increased' : 'decreased';
  const topCategory = categoryAnalysis.categories[0];
  const riskStatus = riskIndicators.overallRiskLevel === 'low' ? 'healthy' : 'needs attention';

  const narrativeInsight = `
Dear ${client.name},

Your statement for ${month} shows a ${balanceDirection} balance of ${balanceMetrics.change}.
You processed ${transactionMetrics.totalTransactions} transactions, with top spending in ${topCategory.name}.

Your account appears ${riskStatus}.
Average daily spending was ${(balanceMetrics.totalOutflow / 31).toFixed(2)},
while incoming funds totaled ${balanceMetrics.totalInflow}.

Consider reviewing your ${topCategory.name} spending patterns and maintaining your current savings rate.
  `.trim();

  return {
    status: 'success',
    source: 'mock',
    timestamp: new Date().toISOString(),
    clientId: client.id,
    month,
    narrativeInsight,
    structuredInsights: extractStructuredInsights(statistics),
    recommendations: generateRecommendations(statistics)
  };
}

/**
 * Extracts structured insights from statistics for API response
 *
 * @private
 * @param {Object} statistics - Computed statistics
 * @returns {Object} Structured insights
 */
function extractStructuredInsights(statistics) {
  const { balanceMetrics, transactionMetrics, categoryAnalysis, trends } = statistics;

  return {
    balanceInsights: {
      totalChange: balanceMetrics.change,
      percentageChange: balanceMetrics.percentageChange,
      direction: balanceMetrics.change > 0 ? 'positive' : 'negative',
      netMonthlyFlow: balanceMetrics.netFlow
    },
    activityInsights: {
      totalActivity: transactionMetrics.totalTransactions,
      completionRate: parseFloat(
        ((transactionMetrics.completedTransactions / transactionMetrics.totalTransactions) * 100).toFixed(2)
      ),
      averageTransactionSize: transactionMetrics.overallMetrics.average,
      largestTransaction: transactionMetrics.overallMetrics.max
    },
    spendingInsights: {
      topSpendingCategory: categoryAnalysis.categories[0]?.name,
      topCategoryAmount: categoryAnalysis.categories[0]?.netAmount,
      secondaryCategory: categoryAnalysis.categories[1]?.name,
      spendingDiversification: categoryAnalysis.totalCategories
    },
    activityPatterns: {
      mostActiveDay: trends.peakDay.date,
      mostActiveCount: trends.peakDay.transactionCount,
      averageDailyActivity: trends.averageTransactionsPerDay
    }
  };
}

/**
 * Generates actionable recommendations based on statistics
 *
 * @private
 * @param {Object} statistics - Computed statistics
 * @returns {Array} Array of recommendations
 */
function generateRecommendations(statistics) {
  const recommendations = [];
  const { balanceMetrics, transactionMetrics, categoryAnalysis, riskIndicators } = statistics;

  // Balance-related recommendations
  if (balanceMetrics.percentageChange < -20) {
    recommendations.push({
      priority: 'high',
      category: 'budget',
      recommendation: 'Your balance decreased significantly this month. Consider reviewing discretionary spending.',
      action: 'Analyze non-essential expenses in your top spending categories'
    });
  }

  if (balanceMetrics.percentageChange > 30) {
    recommendations.push({
      priority: 'medium',
      category: 'savings',
      recommendation: 'Strong positive balance growth this month. Consider setting aside funds for future goals.',
      action: 'Review savings accounts or investment opportunities for surplus funds'
    });
  }

  // Transaction pattern recommendations
  if (transactionMetrics.overallMetrics.standardDeviation > transactionMetrics.overallMetrics.average) {
    recommendations.push({
      priority: 'medium',
      category: 'budget',
      recommendation: 'Your transactions vary significantly in size. Consider budgeting more carefully.',
      action: 'Set spending limits for each category to maintain consistency'
    });
  }

  // Risk-based recommendations
  if (riskIndicators.overallRiskLevel === 'high') {
    recommendations.push({
      priority: 'high',
      category: 'security',
      recommendation: 'Potential issues detected in your account activity.',
      action: `Review alerts: ${riskIndicators.indicators.map(i => i.message).join('; ')}`
    });
  }

  // Category concentration
  const topCategoryPercent = (categoryAnalysis.categories[0]?.transactionCount / transactionMetrics.totalTransactions) * 100;
  if (topCategoryPercent > 40) {
    recommendations.push({
      priority: 'medium',
      category: 'diversification',
      recommendation: `${categoryAnalysis.categories[0]?.name || 'Your top category'} represents ${topCategoryPercent.toFixed(0)}% of activity.`,
      action: 'Consider diversifying your spending patterns'
    });
  }

  // Pending transactions
  if (transactionMetrics.pendingTransactions > 0) {
    recommendations.push({
      priority: 'low',
      category: 'account',
      recommendation: `You have ${transactionMetrics.pendingTransactions} pending transactions.`,
      action: 'Monitor these transactions for completion'
    });
  }

  return recommendations;
}

export default {
  generateClientInsights
};


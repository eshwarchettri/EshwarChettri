/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netelixir.lxrretail.reportdownload;

import com.netelixir.lxrretail.common.CommonFunctions;
import com.netelixir.lxrretail.model.GoogleAnalyticsInformation;
import com.netelixir.lxrretail.common.AutoReportConstants;
import com.netelixir.lxrretail.common.CommonConstants;
import com.netelixir.lxrretail.common.DateConverter;
import com.netelixir.lxrretail.dao.AccountDetailsDao;
import com.netelixir.lxrretail.dao.ReportsAutomationCustomColumnsDao;
import com.netelixir.lxrretail.dao.ReportsAutomationTemplateDao;
import com.netelixir.lxrretail.model.AccountDetails;
import com.netelixir.lxrretail.model.ClientInformation;
import com.netelixir.lxrretail.model.ReportsAutomationCustomColumns;
import com.netelixir.lxrretail.model.ReportsAutomationTemplate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author srilakshmi
 */
@Component
public class StandardOrCustomReports {

    private static final Log LOGGER = LogFactory.getLog(StandardOrCustomReports.class);
    List<ReportsAutomationCustomColumns> colNamesWithTotal = new ArrayList<>();
    List<ReportsAutomationCustomColumns> automationCustomColumnses = new ArrayList<>();
    ReportsAutomationCustomColumnsDao automationCustomColumnsDao = null;
    AccountDetailsDao accountDetailsDao;
    ReportsAutomationCustomColumns automationCustomColumns = null;
    ReportsAutomationTemplate templateInfo;
    ReportsAutomationTemplateDao automationTemplateDao;
    List<AccountDetails> accountDetails;
    List<ClientInformation> clientInformation;
    AutoReportDownloadDao autoReportDownloadDao;
    DriverManagerDataSource dataSource;
    String appPath;
    private ExcelGenerator excelGenerator = null;
    private final String orderType = "CONVERSIONS";
    private long clientId = 0;
    private int weekStart = 0;
    private long templateId = 0;
    private String[] releatedAccIds = null;
    private String reportsPath = "/disk2/lxrretail/reports/";
//    private String selectedMetrics = "impressions,clicks,cost,conversions,total_conv_value";
    private boolean is_mom = false;
    private boolean isSigned = false;

    public boolean getIs_mom() {
        return is_mom;
    }

    public void setIs_mom(boolean is_mom) {
        this.is_mom = is_mom;
    }

    public String getReportColNames() {
        return reportColNames;
    }

    public void setReportColNames(String reportColNames) {
        this.reportColNames = reportColNames;
    }

    public String getTmpTablename() {
        return tmpTablename;
    }

    public String getReportFbColNames() {
        return reportFbColNames;
    }

    public void setReportFbColNames(String reportFbColNames) {
        this.reportFbColNames = reportFbColNames;
    }

    private int durNo = 0;
    private Calendar cal = null;
    private Calendar fcalYOY = null;
    private int fdate = 0;
    private int fmonthNo = 0;
    private String fmonthName = "";
    private String fromDate = "";
    private String fbFromDate = "";
    private String yoyFromDate = "";
    private int tdate = 0;
    private int tyear = 0;
    private int fyear = 0;
    private int tmonthNo = 0;
    private String tmonthName = "";

    private int yoyfdate = 0;
    private int yoyfmonthNo = 0;
    private String yoyfmonthName = "";
//    private String yoyfromDate = "";
    private int yoytdate = 0;
    private int yoytyear = 0;
    private int yoyfyear = 0;
    private int yoytmonthNo = 0;
    private String yoytmonthName = "";

    private String toDate = "";
    private String fbToDate = "";
    private String yoyToDate = "";
    private int weekOfYear = 0;
    private int year = 0;
    private String dateRange = "";
    private String yoyDateRange = "";
    private String reportingDate = "";

    private String preparedOn = "";
    private String fileName = "";
    private String baseFileName = "";
    private StringBuilder sqlQuery = null;
    private String target = "";
    private String gaAccIds = "";
    private String[] accs = null;
    private long gAcc_id = 0;
    private long mAcc_id = 0;
    private long yahAcc_id = 0;
    private long amzAcc_id = 0;
    private long fbAcc_id = 0;
    private long se_accountId;
    private long se_MsnaccountId;
    private long se_yahaccountId;
    private long se_amzAccountId;
    private int amzAccType;
    private int amzCmpType;
    private String metric1 = "";
    private String metric2 = "";
    private String metric3 = "";
    private String metric4 = "";
    private int weeklyLoopCnt = 0;
    private int monthlyLoopCnt = 0;
    private String sqlColumnNames = "";
    private String sqlFbColumnNames = "";
    private String amzSqlColumnNames = "";
    private String basicTotalSqlColumnNames = "";
    private String basicFbTotalSqlColumnNames = "";
    private String amzBasicTotalSqlColumnNames = "";
    private String reportColNames = "";
    private String reportFbColNames = "";
    private List<ReportsAutomationCustomColumns> finalTotals;
    private String fbCampSum;
    private String fbCampDiff;
    private AutoReportsStatsInfo autoReportsStatsCol = null;
//    private List<AutoReportsStatsInfo> autoReportsFbStatsCol = null;
//    private AutoReportsStatsInfo autoReportsFbStatsCol1 = null;
//    private AutoReportsStatsInfo autoReportsFbStatsCol2 = null;
    private String[] revenueSources = null;
    private String[] orderSources = null;
    private String gtax = "";
    private String mtax = "";
    private String ytax = "";
//    private String fbtax = "";
    private String yshipping = "";
//    private String fbshipping = "";
    private String gshipping = "";
    private String mshipping = "";
    private String grevenue = "";
    private String mrevenue = "";
    private String yrevenue = "";
    private String arevenue = "";
    private String hrevenue = "";
    private String fbrevenue = "";
//    private String fbGarevenue = "";
    private String aorders = "";
    private String horders = "";
    private String gorders = "";
    private String morders = "";
    private String yorders = "";
    private String forders = "";
//    private String fGaorders = "";
    private boolean yearOverYear = false;
    private String tmpTablename = "";
    private boolean customDownload = false;
    private String finalStatsQuery = "";
    private String gleseStatsTableName = "";
    private String glegaStatsTableName = "";
    private String msnseStatsTableName = "";
    private String msngaStatsTableName = "";
    private String yahseStatsTableName = "";
    private String yahgaStatsTableName = "";
    private String amzStatsTableName = "Amazon_Report_CampaignStats";
    private String fbStatsTableName = "Facebook_Report_CampaignStats";
    private String durCondition = "";
    private String isMonthTillDate = "";
    Long seAccId = null;
    Long msnseAccId = null;
    Long yahseAccId = null;
    Long fbseAccId = null;

    private StringBuilder fbQuery = null;
    private StringBuilder fbMergeQuery = null;
    private StringBuilder fbMomMergeQuery = null;
    private StringBuilder fbMomQuery = null;
    private StringBuilder totalFbQuery = null;
    private StringBuilder totalMomQuery = null;
    private StringBuilder totalFbMomQuery = null;

    private StringBuilder fbWowQuery = null;
//    private StringBuilder totalFbWeeklyQuery = null;
    private StringBuilder totalWowQuery = null;
    private StringBuilder fbWomMergeQuery = null;

    private StringBuilder amzQuery = null;
    private StringBuilder amzSPQuery = null;
    private StringBuilder amzHSAQuery = null;
    private StringBuilder amzMergeQuery = null;
    private StringBuilder amzSPMergeQuery = null;
    private StringBuilder amzHSAMergeQuery = null;

    private StringBuilder gleQuery = null;
    private StringBuilder msnQuery = null;
    private StringBuilder yahGemQuery = null;

    private StringBuilder gaGleQuery = null;
    private StringBuilder gleYOYQuery = null;
    private StringBuilder gaGleYOYQuery = null;
    private StringBuilder gleMergeQuery = null;
    private StringBuilder gaGleMergeQuery = null;
    private StringBuilder gleYOYMergeQuery = null;
    private StringBuilder gaGleYOYMergeQuery = null;

    private StringBuilder gaMsnQuery = null;
    private StringBuilder msnYOYQuery = null;
    private StringBuilder gaMsnYOYQuery = null;
    private StringBuilder msnMergeQuery = null;
    private StringBuilder gaMsnMergeQuery = null;
    private StringBuilder msnYOYMergeQuery = null;
    private StringBuilder gaMsnYOYMergeQuery = null;

    private StringBuilder gaYahGemQuery = null;
    private StringBuilder yahGemYOYQuery = null;
    private StringBuilder gaYahGemYOYQuery = null;
    private StringBuilder yahGemMergeQuery = null;
    private StringBuilder gaYahGemMergeQuery = null;
    private StringBuilder yahGemYOYMergeQuery = null;
    private StringBuilder gaYahGemYOYMergeQuery = null;

    private StringBuilder createTable = new StringBuilder();
    private boolean tablecreated;
    private boolean tabledatainserted;
    private int[] currStatsRowNos = null;
    private boolean isCustom = false;
    private int fbSeId = 0;
    private String pattern = "yyyy-MM-dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    private String revOrderSrc = "";
    private String SqlCol1 = "";
    private String SqlCol2 = "";
    private String currencySymbol = "";
    private String rptNamePrefix = "";
    private String wkRptNameSuffix = "";
    private String mnRptNameSuffix = "";

    public String getFbCampSum() {
        return fbCampSum;
    }

    public void setFbCampSum(String fbCampSum) {
        this.fbCampSum = fbCampSum;
    }

    public String getFbCampDiff() {
        return fbCampDiff;
    }

    public void setFbCampDiff(String fbCampDiff) {
        this.fbCampDiff = fbCampDiff;
    }

    public boolean isCustomDownload() {
        return customDownload;
    }

    public void setCustomDownload(boolean customDownload) {
        this.customDownload = customDownload;
    }

    public boolean isYearOverYear() {
        return yearOverYear;
    }

    public void setYearOverYear(boolean yearOverYear) {
        this.yearOverYear = yearOverYear;
    }

    public ReportsAutomationTemplate getTemplateInfo() {
        return templateInfo;
    }

    public void setTemplateInfo(ReportsAutomationTemplate templateInfo) {
        this.templateInfo = templateInfo;
    }

    public List<AccountDetails> getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(List<AccountDetails> accountDetails) {
        this.accountDetails = accountDetails;
    }

    public List<ClientInformation> getClientInformation() {
        return clientInformation;
    }

    public void setClientInformation(List<ClientInformation> clientInformation) {
        this.clientInformation = clientInformation;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public StandardOrCustomReports() {
    }

    public StandardOrCustomReports(ReportsAutomationTemplate templateInfo, List<AccountDetails> accountDetails,
            List<ClientInformation> clientInformation, String appPath,
            ReportsAutomationCustomColumnsDao customColumnsDao,
            ReportsAutomationTemplateDao automationTemplateDao, AutoReportDownloadDao autoReportDownloadDao, DriverManagerDataSource dataSource, AccountDetailsDao accountDetailsDao, boolean isSigned, boolean customDownload) {
        this.dataSource = dataSource;
        this.templateInfo = templateInfo;
        this.accountDetailsDao = accountDetailsDao;
        this.automationCustomColumnsDao = customColumnsDao;
        this.automationTemplateDao = automationTemplateDao;
        this.autoReportDownloadDao = autoReportDownloadDao;
        this.accountDetails = accountDetails;
        this.clientInformation = clientInformation;
        this.appPath = appPath;
        clientId = templateInfo.getClientId();
        weekStart = templateInfo.getWeekStart();
//        releatedAccIds = templateInfo.getAccountIds().split(",");
        releatedAccIds = new String[accountDetails.size()];
        for (int k = 0; k < accountDetails.size(); k++) {
            releatedAccIds[k] = accountDetails.get(k).getAccountID().toString();
        }
        templateId = templateInfo.getTemplateId();
        cal = Calendar.getInstance();
        preparedOn = CommonFunctions.getMonthName(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DATE) + ", " + cal.get(Calendar.YEAR);
        target = templateInfo.getTarget();
        this.isSigned = isSigned;
        this.customDownload = customDownload;
        try {
            //For preparing the Account Target text to be printed in the report
            if (target != null) {
                StringBuilder tarKPI = new StringBuilder();
                String[] tarArr = target.split("-");
                if (tarArr.length == 3) {
                    switch (Integer.valueOf(tarArr[0])) {
                        case 1:
                            tarKPI.append("CPO");
                            break;
                        case 2:
                            tarKPI.append("RoAS");
                            break;
                        case 3:
                            tarKPI.append("R/C");
                            break;
                        case 4:
                            tarKPI.append("E/R");
                            break;
                    }
                    switch (Integer.valueOf(tarArr[1])) {
                        case 1:
                            tarKPI.append(" > ");
                            break;
                        case 2:
                            tarKPI.append(" < ");
                            break;
                        case 3:
                            tarKPI.append(" = ");
                            break;
                    }
                    tarKPI.append(tarArr[2]);
                    target = tarKPI.toString();
                }
            }
            //for setting the selected accs
            for (int i = 0; i < accountDetails.size(); i++) {
                if (accountDetails.get(i).getSearchEngineID() == CommonConstants.SE_GOOGLE) {
                    gAcc_id = Long.valueOf(releatedAccIds[i]);
                } else if (accountDetails.get(i).getSearchEngineID() == CommonConstants.SE_BING) {
                    mAcc_id = Long.valueOf(releatedAccIds[i]);
                } else if (accountDetails.get(i).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                    yahAcc_id = Long.valueOf(releatedAccIds[i]);
                }
            }

            for (int i = 0; i < accountDetails.size(); i++) {
                //To get search engine ids
                if (accountDetails.get(i).getAccountID() == gAcc_id) {
                    se_accountId = accountDetails.get(i).getSeAccountId();
                } else if (accountDetails.get(i).getAccountID() == mAcc_id) {
                    se_MsnaccountId = accountDetails.get(i).getSeAccountId();
                } else if (accountDetails.get(i).getAccountID() == yahAcc_id) {
                    se_yahaccountId = accountDetails.get(i).getSeAccountId();
                }
            }

            if (templateInfo.getGoogleAnalytics() == 3) {
                gaAccIds = gAcc_id + "," + mAcc_id + "," + yahAcc_id;
            } else if (templateInfo.getGoogleAnalytics() == 1) {
                gaAccIds = "" + gAcc_id;
//            gaAccIds = gAcc_id + "," + mAcc_id;
            } else if (templateInfo.getGoogleAnalytics() == 2) {
                gaAccIds = "" + mAcc_id;
            } else if (templateInfo.getGoogleAnalytics() == 10) {
                gaAccIds = "" + yahAcc_id;
            } else if (templateInfo.getGoogleAnalytics() == 6) {
                gaAccIds = gAcc_id + "," + mAcc_id;
            } else if (templateInfo.getGoogleAnalytics() == 4) {
                gaAccIds = gAcc_id + "," + yahAcc_id;
            } else if (templateInfo.getGoogleAnalytics() == 5) {
                gaAccIds = mAcc_id + "," + yahAcc_id;
            }

            accs = templateInfo.getAccountIds().split(",");
            automationCustomColumnses = automationCustomColumnsDao.getGroupData("select * from lxr_reportsauto_customcolumns where client_id in(-1," + clientInformation.get(0).getClientID() + ")");

            int metricCnt = 1;
            if (templateInfo.getChartMetrics() != null) {
                String chartMetrics[] = templateInfo.getChartMetrics().split(";");
                for (int i = 0; i < chartMetrics.length; i++) {
                    String metrics[] = chartMetrics[i].split(",");
                    if (metricCnt == 1) {
                        metric1 = metrics[0];
                        metric2 = metrics[1];
                    } else {
                        metric3 = metrics[0];
                        metric4 = metrics[1];
                    }
                    metricCnt++;
                }
            }
            if (accountDetails.size() > 0) {
                currencySymbol = accountDetails.get(0).getCurrencySymbol();
            }
            if (currencySymbol == null) {
                currencySymbol = "";
            }
            weeklyLoopCnt = Integer.parseInt(templateInfo.getTrend().split(",")[0]) + 1; //  trend for weeks or month in the report (like three weeks and four months)
            monthlyLoopCnt = Integer.parseInt(templateInfo.getTrend().split(",")[1]) + 1;
            sqlColumnNames = getSqlColumnNames();
//         if (accs.length > 1) {
            if (!templateInfo.getAccountIds().equals("")) {
                sqlColumnNames = sqlColumnNames.substring(0, sqlColumnNames.length() - 1);
                // G+B+Y selected
                if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id != 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "(ifnull(gimpressions,0)+ifnull(mimpressions,0)+ifnull(yimpressions,0)) as impressions ,"
                            + "(ifnull(gclicks,0)+ifnull(mclicks,0)+ifnull(yclicks,0)) as clicks,(ifnull(gcost,0)+ifnull(mcost,0)+ifnull(ycost,0)) as cost,"
                            + "(ifnull(" + grevenue + ",0)+ifnull(" + mrevenue + ",0)+ifnull(" + yrevenue + ",0)) as revenue,"
                            + "(ifnull(" + gorders + ",0)+ifnull(" + morders + ",0)+ifnull(" + yorders + ",0)) as orders,case (ifnull(gimpressions,0)+ifnull(mimpressions,0)+ifnull(yimpressions,0)) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))+(ifnull(mavg_pos,0)*ifnull(mimpressions,0))+(ifnull(yavg_pos,0)*ifnull(yimpressions,0)))/(ifnull(gimpressions,0)+ifnull(mimpressions,0)+ifnull(yimpressions,0))) end as avg_pos";
                }
                if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id != 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "(ifnull(gimpressions,0)+ifnull(mimpressions,0)+ifnull(yimpressions,0)) as impressions ,"
                            + "(ifnull(gclicks,0)+ifnull(mclicks,0)+ifnull(yclicks,0)) as clicks,(ifnull(gcost,0)+ifnull(mcost,0)+ifnull(ycost,0)) as cost,"
                            + "(ifnull(" + grevenue + ",0)+ifnull(" + mrevenue + ",0)+ifnull(" + yrevenue + ",0)) as revenue, "
                            + "(ifnull(" + gorders + ",0)+ifnull(" + morders + ",0)+ifnull(" + yorders + ",0)) as orders,"
                            + "(ifnull(" + gtax + ",0)+ifnull(" + mtax + ",0)+ifnull(" + ytax + ",0)) as tax,"
                            + "(ifnull(" + gshipping + ",0)+ifnull(" + mshipping + ",0)+ifnull(" + yshipping + ",0)) as shipping,"
                            + "case (ifnull(gimpressions,0)+ifnull(mimpressions,0)+ifnull(yimpressions,0)) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))+(ifnull(mavg_pos,0)*ifnull(mimpressions,0))+(ifnull(yavg_pos,0)*ifnull(yimpressions,0)))/(ifnull(gimpressions,0)+ifnull(mimpressions,0)+ifnull(yimpressions,0))) end as avg_pos";
                } // G+B selected
                else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id == 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "(ifnull(gimpressions,0)+ifnull(mimpressions,0)) as impressions ,(ifnull(gclicks,0)+ifnull(mclicks,0)) as clicks,"
                            + "(ifnull(gcost,0)+ifnull(mcost,0)) as cost,(ifnull(" + grevenue + ",0)+ifnull(" + mrevenue + ",0)) as revenue,"
                            + "(ifnull(" + gorders + ",0)+ifnull(" + morders + ",0)) as orders,"
                            + "case (ifnull(gimpressions,0)+ifnull(mimpressions,0)) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))+(ifnull(mavg_pos,0)*ifnull(mimpressions,0)))/(ifnull(gimpressions,0)+ifnull(mimpressions,0))) end as avg_pos";
                } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id == 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "(ifnull(gimpressions,0)+ifnull(mimpressions,0)) as impressions ,"
                            + "(ifnull(gclicks,0)+ifnull(mclicks,0)) as clicks,(ifnull(gcost,0)+ifnull(mcost,0)) as cost,"
                            + "(ifnull(" + grevenue + ",0)+ifnull(" + mrevenue + ",0)) as revenue,(ifnull(" + gorders + ",0)+ifnull(" + morders + ",0)) as orders,"
                            + "(ifnull(" + gtax + ",0)+ifnull(" + mtax + ",0)) as tax,(ifnull(" + gshipping + ",0)+ifnull(" + mshipping + ",0)) as shipping,"
                            + "case (ifnull(gimpressions,0)+ifnull(mimpressions,0)) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))+(ifnull(mavg_pos,0)*ifnull(mimpressions,0)))/(ifnull(gimpressions,0)+ifnull(mimpressions,0))) end as avg_pos";
                } // G+Y selected
                else if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id != 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "(ifnull(gimpressions,0)+ifnull(yimpressions,0)) as impressions ,(ifnull(gclicks,0)+ifnull(yclicks,0)) as clicks,"
                            + "(ifnull(gcost,0)+ifnull(ycost,0)) as cost,(ifnull(" + grevenue + ",0)+ifnull(" + yrevenue + ",0)) as revenue,"
                            + "(ifnull(" + gorders + ",0)+ifnull(" + yorders + ",0)) as orders,"
                            + "case (ifnull(gimpressions,0)+ifnull(yimpressions,0)) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))+(ifnull(yavg_pos,0)*ifnull(yimpressions,0)))/(ifnull(gimpressions,0)+ifnull(yimpressions,0))) end as avg_pos";
                } else if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id != 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "(ifnull(gimpressions,0)+ifnull(yimpressions,0)) as impressions ,(ifnull(gclicks,0)+ifnull(yclicks,0)) as clicks,"
                            + "(ifnull(gcost,0)+ifnull(ycost,0)) as cost,(ifnull(" + grevenue + ",0)+ifnull(" + yrevenue + ",0)) as revenue,"
                            + "(ifnull(" + gorders + ",0)+ifnull(" + yorders + ",0)) as orders,"
                            + "(ifnull(" + gtax + ",0)+ifnull(" + ytax + ",0)) as tax,"
                            + "(ifnull(" + gshipping + ",0)+ifnull(" + yshipping + ",0)) as shipping,"
                            + "case (ifnull(gimpressions,0)+ifnull(yimpressions,0)) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))+(ifnull(yavg_pos,0)*ifnull(yimpressions,0)))/(ifnull(gimpressions,0)+ifnull(yimpressions,0))) end as avg_pos";
                } // B+Y selected
                else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id != 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "(ifnull(mimpressions,0)+ifnull(yimpressions,0)) as impressions ,(ifnull(mclicks,0)+ifnull(yclicks,0)) as clicks,"
                            + "(ifnull(mcost,0)+ifnull(ycost,0)) as cost,(ifnull(" + mrevenue + ",0)+ifnull(" + yrevenue + ",0)) as revenue,"
                            + "(ifnull(" + morders + ",0)+ifnull(" + yorders + ",0)) as orders,"
                            + "case (ifnull(mimpressions,0)+ifnull(yimpressions,0)) when 0 then 0 else (((ifnull(mavg_pos,0)*ifnull(mimpressions,0))+(ifnull(yavg_pos,0)*ifnull(yimpressions,0)))/(ifnull(mimpressions,0)+ifnull(yimpressions,0))) end as avg_pos";
                } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id != 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "(ifnull(mimpressions,0)+ifnull(yimpressions,0)) as impressions ,(ifnull(mclicks,0)+ifnull(yclicks,0)) as clicks,"
                            + "(ifnull(mcost,0)+ifnull(ycost,0)) as cost,(ifnull(" + mrevenue + ",0)+ifnull(" + yrevenue + ",0)) as revenue,"
                            + "(ifnull(" + morders + ",0)+ifnull(" + yorders + ",0)) as orders,"
                            + "(ifnull(" + mtax + ",0)+ifnull(" + mtax + ",0)) as tax,"
                            + "(ifnull(" + mshipping + ",0)+ifnull(" + mshipping + ",0)) as shipping,"
                            + "case (ifnull(mimpressions,0)+ifnull(yimpressions,0)) when 0 then 0 else (((ifnull(mavg_pos,0)*ifnull(mimpressions,0))+(ifnull(yavg_pos,0)*ifnull(yimpressions,0)))/(ifnull(mimpressions,0)+ifnull(yimpressions,0))) end as avg_pos";
                } //        } //only GLE Selected
                else if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id == 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "ifnull(gimpressions,0) as impressions ,ifnull(gclicks,0) as clicks,ifnull(gcost,0) as cost,ifnull(" + grevenue + ",0) as revenue,ifnull(" + gorders + ",0) as orders,"
                            + "case ifnull(gimpressions,0) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))/ifnull(gimpressions,0))) end as avg_pos";
                } else if (gAcc_id != 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "ifnull(gimpressions,0) as impressions ,ifnull(gclicks,0) as clicks,ifnull(gcost,0) as cost,"
                            + "ifnull(" + grevenue + ",0) as revenue,"
                            + "ifnull(" + gtax + ",0) as tax, ifnull(" + gshipping + ",0) as shipping,ifnull(" + gorders + ",0) as orders,"
                            + "case ifnull(gimpressions,0) when 0 then 0 else (((ifnull(gavg_pos,0)*ifnull(gimpressions,0))/ifnull(gimpressions,0))) end as avg_pos";
                } //only MSN selected
                else if (mAcc_id != 0 && gAcc_id == 0 && yahAcc_id == 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "ifnull(mimpressions,0) as impressions ,ifnull(mclicks,0) as clicks,ifnull(mcost,0) as cost,ifnull(" + mrevenue + ",0) as revenue,ifnull(" + morders + ",0) as orders,"
                            + "case ifnull(mimpressions,0) when 0 then 0 else (((ifnull(mavg_pos,0)*ifnull(mimpressions,0))/ifnull(mimpressions,0))) end as avg_pos";
                } else if (mAcc_id != 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "ifnull(mimpressions,0) as impressions ,ifnull(mclicks,0) as clicks,ifnull(mcost,0) as cost,"
                            + "ifnull(" + mrevenue + ",0) as revenue,ifnull(" + morders + ",0) as orders, ifnull(" + mtax + ",0) as tax, ifnull(" + yshipping + ",0) as shipping,"
                            + "case ifnull(mimpressions,0) when 0 then 0 else (((ifnull(mavg_pos,0)*ifnull(mimpressions,0))/ifnull(mimpressions,0))) end as avg_pos";
                } //only YG selected
                else if (yahAcc_id != 0 && gAcc_id == 0 && mAcc_id == 0 && templateInfo.getRevenueType() == 1) {
                    basicTotalSqlColumnNames = "ifnull(yimpressions,0) as impressions ,ifnull(yclicks,0) as clicks,ifnull(ycost,0) as cost,ifnull(" + yrevenue + ",0) as revenue,ifnull(" + yorders + ",0) as orders,"
                            + "case ifnull(yimpressions,0) when 0 then 0 else (((ifnull(yavg_pos,0)*ifnull(yimpressions,0))/ifnull(yimpressions,0))) end as avg_pos";
                } else if (yahAcc_id != 0 && templateInfo.getRevenueType() == 2) {
                    basicTotalSqlColumnNames = "ifnull(yimpressions,0) as impressions ,ifnull(yclicks,0) as clicks,ifnull(ycost,0) as cost,"
                            + "ifnull(" + yrevenue + ",0) as revenue,ifnull(" + yorders + ",0) as orders, ifnull(" + ytax + ",0) as tax, ifnull(" + yshipping + ",0) as shipping,"
                            + "case ifnull(yimpressions,0) when 0 then 0 else (((ifnull(yavg_pos,0)*ifnull(yimpressions,0))/ifnull(yimpressions,0))) end as avg_pos";
                }
            }
            rptNamePrefix = clientInformation.get(0).getWebsite().split("\\.")[1].substring(0, 3).toUpperCase();
            cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, weekStart);
            if (cal.after(Calendar.getInstance())) {
                cal.add(Calendar.DAY_OF_WEEK, -7);
            }
            wkRptNameSuffix = new DateConverter(cal).getDateMMDDYYYYFormat();
            cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 3);
            mnRptNameSuffix = new DateConverter(cal).getDateMMDDYYYYFormat();
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

    private String getSqlColumnNames() {
        String sql = "select METRICS from ne_reportautomation_template where TEMPLATE_ID = '" + templateId + "'";
        String columnIds = automationTemplateDao.getColumnsIdsList(sql);
        return prepareColumnNames(columnIds);
    }

    private String prepareColumnNames(String colmnIds) {
        String finalSqlColsName = "";
        try {
            boolean currencyFlag = false;
            ArrayList sqlCols = new ArrayList();
            String strCol = "";
            String strFormulaCol = "";
            String prefix = "";
            reportColNames = "";

            String[] columnIds = colmnIds.split(",");
            revenueSources = templateInfo.getRevenueSources().split(","); // revenue sources 4.4 for facebook
            orderSources = templateInfo.getOrderSources().split(","); // order sources 4.4 for facebook
            String account_id = "";
            if (accs.length == 1) {
                account_id = accs[0];
            }
            if (accs.length == 2) {
                for (int j = 0; j < accs.length; j++) {
                    if (j == 0 && automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_BING) {
                        account_id = accs[1];
                        accs[1] = accs[j];
                        accs[0] = account_id;
                    } else if (j == 0 && automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                        account_id = accs[1];
                        accs[1] = accs[j];
                        accs[0] = account_id;
                    }
                }
            }
            if (accs.length == 3) {
                for (int j = 0; j < accs.length; j++) {
                    if (j == 0 && automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_BING) {
                        if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                            account_id = accs[j];
                            accs[0] = accs[2];
                            accs[2] = accs[1];
                            accs[1] = account_id;
                        } else if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_GOOGLE) {
                            account_id = accs[j];
                            accs[0] = accs[1];
                            accs[1] = account_id;
                        }
                    } else if (j == 0 && automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                        if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_BING) {
                            account_id = accs[j];
                            accs[0] = accs[2];
                            accs[2] = account_id;
                        } else if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_GOOGLE) {
                            account_id = accs[j];
                            accs[0] = accs[1];
                            accs[1] = accs[2];
                            accs[2] = account_id;
                        }
                    } else if (j == 0 && automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_GOOGLE) {
                        if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_BING) {
                            //do nothing correct order only
                        } else if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                            account_id = accs[1];
                            accs[1] = accs[2];
                            accs[2] = account_id;
                        }
                    }
                }
            }
            for (int i = 0; i < revenueSources.length; i++) {
                // based on the source it will arrange the filed name for the query.
                if (!revenueSources[i].equalsIgnoreCase("")) {
                    if (revenueSources[i].contains(AutoReportConstants.REPORTS_SOURCE_GOOGLE + ".")) {
                        if (!revOrderSrc.equalsIgnoreCase("")) {
                            revOrderSrc += ",";
                        }
                        if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE)) {
                            grevenue = "gRevenue";
                            revOrderSrc += "Google-Google Adwords";
                        } else if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS) && templateInfo.getRevenueType() == 1) {
                            grevenue = "gagleRevenue";
                            revOrderSrc += "Google-Google Analytics";
                        } else if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS) && templateInfo.getRevenueType() == 2) {
                            grevenue = "gaglePRevenue";
                            gtax = "gagleTax";
                            gshipping = "gagleShipping";
                            revOrderSrc += "Google-Google Analytics";
                        }
                    } else if (revenueSources[i].contains(AutoReportConstants.REPORTS_SOURCE_MSN + ".")) {
                        if (!revOrderSrc.equalsIgnoreCase("")) {
                            revOrderSrc += ",";
                        }
                        if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_MSN)) {
                            mrevenue = "mRevenue";
                            revOrderSrc += "Bing-Bing Ads";
                        } else if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS) && templateInfo.getRevenueType() == 1) {
                            mrevenue = "gamsnRevenue";
                            revOrderSrc += "Bing-Google Analytics";
                        } else if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS) && templateInfo.getRevenueType() == 2) {
                            mrevenue = "gamsnPRevenue";
                            mtax = "gamsnTax";
                            mshipping = "gamsnShipping";
                            revOrderSrc += "Bing-Google Analytics";
                        }

                    } else if (revenueSources[i].contains(AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI + ".")) {
                        if (!revOrderSrc.equalsIgnoreCase("")) {
                            revOrderSrc += ",";
                        }
                        if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI)) {
                            yrevenue = "yRevenue";
                            revOrderSrc += "YahooGemini-YahooGemini Ads";
                        } else if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS) && templateInfo.getRevenueType() == 1) {
                            yrevenue = "gayahRevenue";
                            revOrderSrc += "YahooGemini-Google Analytics";
                        } else if (revenueSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS) && templateInfo.getRevenueType() == 2) {
                            yrevenue = "gayahPRevenue";
                            ytax = "gayahTax";
                            yshipping = "gayahShipping";
                            revOrderSrc += "YahooGemini-Google Analytics";
                        }
                    }
                }
            }

            for (int i = 0; i < orderSources.length; i++) {
                // based on the order source it will arrange the field names for the query.
                if (!orderSources[i].equalsIgnoreCase("")) {
                    if (orderSources[i].contains(AutoReportConstants.REPORTS_SOURCE_GOOGLE + ".")) {
                        if (orderSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE)) {
                            gorders = "gOrders";
                        } else if (orderSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS)) {
                            gorders = "gagleOrders";
                        }
                    }
                    if (orderSources[i].contains(AutoReportConstants.REPORTS_SOURCE_MSN + ".")) {
                        if (orderSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_MSN)) {
                            morders = "mOrders";
                        } else if (orderSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS)) {
                            morders = "gamsnOrders";
                        }
                    }
                    if (orderSources[i].contains(AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI + ".")) {
                        if (orderSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI)) {
                            yorders = "yOrders";
                        } else if (orderSources[i].contains("." + AutoReportConstants.REPORTS_SOURCE_GOOGLE_ANALYTICS)) {
                            yorders = "gayahOrders";
                        }
                    }
                }
            }
// this forloop is for custom metricss. here it will assingned name for the numbers , such as impressions , clicks cost etc...
            if (!templateInfo.getAccountIds().equals("")) {
                for (int i = 0; i < columnIds.length; i++) {
                    strCol = "";
                    String formula;
                    SqlCol1 = "";
                    SqlCol2 = "";
                    // Custom formulas
                    ReportsAutomationCustomColumns colNames = new ReportsAutomationCustomColumns();
                    automationCustomColumns = automationCustomColumnsDao.getObject(Long.parseLong(columnIds[i]));
                    if (automationCustomColumns.getType() == 0 || automationCustomColumns.getType() == 1) {
                        if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.COST_NAME)) {
                            reportColNames = reportColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                            currencyFlag = true;
                        } else {
                            reportColNames = reportColNames + automationCustomColumns.getVariableName() + ",";
                        }
                    } else {
                        formula = getCustomSqlColumnNameForTotals(automationCustomColumns);
                        automationCustomColumns.setFormula(formula);
                        if (automationCustomColumns.getFormula().contains(AutoReportConstants.REVENUE_NAME) && automationCustomColumns.getFormula().contains(AutoReportConstants.COST_NAME) && automationCustomColumns.getFormula().contains("/")) {
                            reportColNames = reportColNames + automationCustomColumns.getVariableName() + ",";
                        } else if (automationCustomColumns.getFormula().contains(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getFormula().contains(AutoReportConstants.COST_NAME)) {
                            reportColNames = reportColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                            currencyFlag = true;
                        } else if (automationCustomColumns.getFormula().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getFormula().equalsIgnoreCase(AutoReportConstants.COST_NAME)) {
                            reportColNames = reportColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                            currencyFlag = true;
                        } else {
                            reportColNames = reportColNames + automationCustomColumns.getVariableName() + ",";
                        }
                    }
                    // the below code is used to construct custom metrics header names
                    if (automationCustomColumns.getType() == 3) {
                        for (int j = 0; j < accs.length; j++) {
                            //check se_ids of using account_id
                            if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_GOOGLE) {
                                strFormulaCol = getCustomSqlColumnName(AutoReportConstants.REPORTS_SOURCE_GOOGLE);
                                if (strCol.equalsIgnoreCase("")) {
                                    if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME) || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {
                                        strCol = "case " + SqlCol2 + " when 0 then 0 else ";
                                        if (automationCustomColumns.getUnits() == 1) {
                                            strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                                        } else {
                                            strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                                        }
                                    } else if (automationCustomColumns.getUnits() == 1) {
                                        strCol = "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                                    } else {
                                        strCol = strFormulaCol;
                                    }
                                } else if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME)
                                        || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {

                                    if (automationCustomColumns.getUnits() == 1) {

                                        strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                                    } else {
                                        strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                                    }
                                } else if (automationCustomColumns.getUnits() == 1) {

                                    strCol = strCol + "," + "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                                } else {
                                    strCol = strCol + "," + strFormulaCol;
                                }
                            } else if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_BING) {
                                strFormulaCol = getCustomSqlColumnName(AutoReportConstants.REPORTS_SOURCE_MSN);
                                if (strCol.equalsIgnoreCase("")) {
                                    if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME) || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {
                                        strCol = "case " + SqlCol2 + " when 0 then 0 else ";
                                        if (automationCustomColumns.getUnits() == 1) {
                                            strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                                        } else {
                                            strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                                        }
                                    } else if (automationCustomColumns.getUnits() == 1) {
                                        strCol = "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                                    } else {
                                        strCol = strFormulaCol;
                                    }
                                } else if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME)
                                        || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {

                                    if (automationCustomColumns.getUnits() == 1) {

                                        strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                                    } else {
                                        strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                                    }
                                } else if (automationCustomColumns.getUnits() == 1) {

                                    strCol = strCol + "," + "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                                } else {
                                    strCol = strCol + "," + strFormulaCol;
                                }
                            } else if (automationTemplateDao.getClientAndSeId(Long.parseLong(accs[j])).get(0).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                                strFormulaCol = getCustomSqlColumnName(AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI);
                                if (strCol.equalsIgnoreCase("")) {
                                    if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME) || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {
                                        strCol = "case " + SqlCol2 + " when 0 then 0 else ";
                                        if (automationCustomColumns.getUnits() == 1) {
                                            strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                                        } else {
                                            strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                                        }
                                    } else if (automationCustomColumns.getUnits() == 1) {
                                        strCol = "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                                    } else {
                                        strCol = strFormulaCol;
                                    }
                                } else if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME)
                                        || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {

                                    if (automationCustomColumns.getUnits() == 1) {

                                        strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                                    } else {
                                        strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                                    }
                                } else if (automationCustomColumns.getUnits() == 1) {

                                    strCol = strCol + "," + "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                                } else {
                                    strCol = strCol + "," + strFormulaCol;
                                }
                            }
                        }
                        sqlCols.add(strCol);

                    } else {
                        for (int j = 0; j < accountDetails.size(); j++) {
                            //check se_ids of using account_id
                            if (accountDetails.get(j).getSearchEngineID() == CommonConstants.SE_GOOGLE) {
                                if (automationCustomColumns.getColumnId() == 7) {
                                    if (strCol.equalsIgnoreCase("")) {
                                        if (automationCustomColumns.getVariableName().contains("/") || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ROAS_NAME) || automationCustomColumns.getVariableName().contains(AutoReportConstants.COST_NAME)) {
                                            strCol = "case gcost when 0 then 0 else ((" + grevenue + "- gcost)/gcost)" + " end as \"gROAS\"";

                                        }
                                    } else if (automationCustomColumns.getVariableName().contains("/") || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ROAS_NAME) || automationCustomColumns.getVariableName().contains(AutoReportConstants.COST_NAME)) {
                                        strCol = strCol + "," + "case gcost when 0 then 0 else ((" + grevenue + "- gcost)/gcost)" + " end as \"gROAS\"";
                                    }
                                } else if (strCol.equalsIgnoreCase("")) {
                                    strCol = getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_GOOGLE);
                                } else {
                                    strCol = strCol + "," + getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_GOOGLE);
                                }
                            }
                            if (accountDetails.get(j).getSearchEngineID() == CommonConstants.SE_BING) {
                                if (automationCustomColumns.getColumnId() == 7) {
                                    if (strCol.equalsIgnoreCase("")) {
                                        if (automationCustomColumns.getVariableName().contains("/") || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ROAS_NAME) || automationCustomColumns.getVariableName().contains(AutoReportConstants.COST_NAME)) {
                                            strCol = "case mcost when 0 then 0 else ((" + mrevenue + "- mcost)/mcost)" + " end as \"mROAS\"";

                                        }
                                    } else if (automationCustomColumns.getVariableName().contains("/") || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ROAS_NAME) || automationCustomColumns.getVariableName().contains(AutoReportConstants.COST_NAME)) {
                                        strCol = strCol + "," + "case mcost when 0 then 0 else ((" + mrevenue + "- mcost)/mcost)" + " end as \"mROAS\"";
                                    }
                                } else if (strCol.equalsIgnoreCase("")) {
                                    strCol = getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_MSN);
                                } else {
                                    strCol = strCol + "," + getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_MSN);
                                }
                            }
                            if (accountDetails.get(j).getSearchEngineID() == CommonConstants.SE_YAHOO) {
                                if (automationCustomColumns.getColumnId() == 7) {
                                    if (strCol.equalsIgnoreCase("")) {
                                        if (automationCustomColumns.getVariableName().contains("/") || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ROAS_NAME) || automationCustomColumns.getVariableName().contains(AutoReportConstants.COST_NAME)) {
                                            strCol = "case ycost when 0 then 0 else ((" + yrevenue + "- ycost)/ycost)" + " end as \"yROAS\"";

                                        }
                                    } else if (automationCustomColumns.getVariableName().contains("/") || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ROAS_NAME) || automationCustomColumns.getVariableName().contains(AutoReportConstants.COST_NAME)) {
                                        strCol = strCol + "," + "case ycost when 0 then 0 else ((" + yrevenue + "- ycost)/ycost)" + " end as \"yROAS\"";
                                    }
                                } else if (strCol.equalsIgnoreCase("")) {
                                    strCol = getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI);
                                } else {
                                    strCol = strCol + "," + getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI);
                                }
                            }
                        }
                        sqlCols.add(strCol);
                    }
                    colNames.setVariableName(automationCustomColumns.getVariableName());
                    colNames.setFormula(getCustomSqlColumnNameForTotals(automationCustomColumns));
                    colNames.setType(automationCustomColumns.getType());
                    colNames.setColumnId(automationCustomColumns.getColumnId());
                    if (currencyFlag) {
                        colNames.setCurrencySymbol(currencySymbol);
                    }
//            currencyFlag = false;
                    colNamesWithTotal.add(colNames);
                }
            }
            for (int j = 0; j < sqlCols.size(); j++) {
                finalSqlColsName = finalSqlColsName + sqlCols.get(j) + ",";
            }
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return finalSqlColsName;

    }

    private String getAmzSqlColumnNames() {
        String sql = "select METRICS from ne_reportautomation_template where TEMPLATE_ID = '" + templateId + "'";
        String columnIds = automationTemplateDao.getColumnsIdsList(sql);

        String nonAmzMetrics = "6,7,20,21";
        List<String> nonAmzCol = Arrays.asList(nonAmzMetrics.split(","));
        List<String> newAmzCol = new ArrayList<>(Arrays.asList(columnIds.split(",")));
        newAmzCol.removeAll(nonAmzCol);
        columnIds = newAmzCol.stream().collect(Collectors.joining(","));

        return prepareAmazonColumnNames(columnIds);
    }

    private String prepareAmazonColumnNames(String colmnIds) {
        boolean currencyFlag = false;
        ArrayList sqlCols = new ArrayList();
        String strCol = "";
        String strFormulaCol = "";
        String prefix = "";
        reportColNames = "";
        String finalSqlColsName = "";
        String[] columnIds = colmnIds.split(",");
        revenueSources = templateInfo.getRevenueSources().split(",");
        orderSources = templateInfo.getOrderSources().split(",");
        aorders = "aOrders";
        horders = "hOrders";
        arevenue = "aRevenue";
        hrevenue = "hRevenue";
        revOrderSrc += "Amazon-Amazon Ads";

// this forloop is for custom metricss. here it will assingned name for the numbers , such as impressions , clicks cost etc...
        for (int i = 0; i < columnIds.length; i++) {
            strCol = "";
            String formula;
            SqlCol1 = "";
            SqlCol2 = "";
            // Custom formulas
            ReportsAutomationCustomColumns colNames = new ReportsAutomationCustomColumns();
            automationCustomColumns = automationCustomColumnsDao.getObject(Long.parseLong(columnIds[i]));
            if (automationCustomColumns.getType() == 0 || automationCustomColumns.getType() == 1) {
                if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.COST_NAME)) {
                    reportColNames = reportColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                    currencyFlag = true;
                } else {
                    reportColNames = reportColNames + automationCustomColumns.getVariableName() + ",";
                }
            } else {
                formula = getCustomSqlColumnNameForTotals(automationCustomColumns);
                automationCustomColumns.setFormula(formula);
                if (automationCustomColumns.getFormula().contains(AutoReportConstants.REVENUE_NAME) && automationCustomColumns.getFormula().contains(AutoReportConstants.COST_NAME) && automationCustomColumns.getFormula().contains("/")) {
                    reportColNames = reportColNames + automationCustomColumns.getVariableName() + ",";
                } else if (automationCustomColumns.getFormula().contains(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getFormula().contains(AutoReportConstants.COST_NAME)) {
                    reportColNames = reportColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                    currencyFlag = true;
                } else if (automationCustomColumns.getFormula().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getFormula().equalsIgnoreCase(AutoReportConstants.COST_NAME)) {
                    reportColNames = reportColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                    currencyFlag = true;
                } else {
                    reportColNames = reportColNames + automationCustomColumns.getVariableName() + ",";
                }
            }

            if (automationCustomColumns.getType() == 3) {
//                //For SPA
                if (amzAccType == 1) {
                    strFormulaCol = getCustomSqlColumnName(AutoReportConstants.REPORTS_SOURCE_AMAZON);
                    if (strCol.equalsIgnoreCase("")) {
                        if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME) || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {
                            strCol = "case " + SqlCol2 + " when 0 then 0 else ";
                            if (automationCustomColumns.getUnits() == 1) {
                                strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                            } else {
                                strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                            }
                        } else if (automationCustomColumns.getUnits() == 1) {
                            strCol = "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                        } else {
                            strCol = strFormulaCol;
                        }
                    } else if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME)
                            || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {

                        if (automationCustomColumns.getUnits() == 1) {
                            strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                        } else {
                            strCol = strCol + "," + "case " + SqlCol2 + "when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                        }
                    } else if (automationCustomColumns.getUnits() == 1) {

                        strCol = strCol + "," + "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                    } else {
                        strCol = strCol + "," + strFormulaCol;
                    }
                    sqlCols.add(strCol);
                } else {
//              //For SPA 
                    amzCmpType = CommonConstants.SPONSOREDPRODUCTS;
                    strFormulaCol = getCustomSqlColumnName(AutoReportConstants.REPORTS_SOURCE_AMAZON);
                    if (strCol.equalsIgnoreCase("")) {
                        if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME) || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {
                            strCol = "case " + SqlCol2 + " when 0 then 0 else ";
                            if (automationCustomColumns.getUnits() == 1) {
                                strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                            } else {
                                strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                            }
                        } else if (automationCustomColumns.getUnits() == 1) {
                            strCol = "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                        } else {
                            strCol = strFormulaCol;
                        }
                    } else if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME)
                            || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {

                        if (automationCustomColumns.getUnits() == 1) {
                            strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                        } else {
                            strCol = strCol + "," + "case " + SqlCol2 + "when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                        }
                    } else if (automationCustomColumns.getUnits() == 1) {

                        strCol = strCol + "," + "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                    } else {
                        strCol = strCol + "," + strFormulaCol;
                    }
                    sqlCols.add(strCol);
//              //For HSA
                    amzCmpType = CommonConstants.HEADLINESEARCH;
                    strCol = "";
                    strFormulaCol = getCustomSqlColumnName(AutoReportConstants.REPORTS_SOURCE_AMAZON);
                    if (strCol.equalsIgnoreCase("")) {
                        if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME) || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {
                            strCol = "case " + SqlCol2 + " when 0 then 0 else ";
                            if (automationCustomColumns.getUnits() == 1) {
                                strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                            } else {
                                strCol = strCol + "(" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                            }
                        } else if (automationCustomColumns.getUnits() == 1) {
                            strCol = "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                        } else {
                            strCol = strFormulaCol;
                        }
                    } else if (strFormulaCol.contains("/") || strFormulaCol.contains(AutoReportConstants.REVENUE_NAME)
                            || strFormulaCol.contains(AutoReportConstants.COST_NAME)) {

                        if (automationCustomColumns.getUnits() == 1) {
                            strCol = strCol + "," + "case " + SqlCol2 + " when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + " *100\'";
                        } else {
                            strCol = strCol + "," + "case " + SqlCol2 + "when 0 then 0 else (" + strFormulaCol + ") end as \'" + strFormulaCol + "\'";
                        }
                    } else if (automationCustomColumns.getUnits() == 1) {

                        strCol = strCol + "," + "(" + strFormulaCol + ") as \'" + strFormulaCol + " *100\'";
                    } else {
                        strCol = strCol + "," + strFormulaCol;
                    }
                    sqlCols.add(strCol);
                }
            } else {
                if (strCol.equalsIgnoreCase("")) {
                    strCol = getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_AMAZON);
                } else {
                    strCol = strCol + "," + getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_AMAZON);
                }
                sqlCols.add(strCol);
            }
            colNames.setVariableName(automationCustomColumns.getVariableName());
            colNames.setFormula(getCustomSqlColumnNameForTotals(automationCustomColumns));
            colNames.setType(automationCustomColumns.getType());
            colNames.setColumnId(automationCustomColumns.getColumnId());
            if (currencyFlag) {
                colNames.setCurrencySymbol(currencySymbol);
            }
            colNamesWithTotal.add(colNames);
        }
        for (int j = 0; j < sqlCols.size(); j++) {
            finalSqlColsName = finalSqlColsName + sqlCols.get(j) + ",";
        }
        return finalSqlColsName;
    }

//    private String getFbSqlColumnNames() {
//        String columnIds = "1,2,3,4,5,9,8,10,11";
//        return prepareFacebookColumnNames(columnIds);
//    }    
    private String prepareFacebookColumnNames(String colmnIds) {
        boolean currencyFlag = false;
        ArrayList sqlCols = new ArrayList();
        String strCol = "";
        String strFormulaCol = "";
        reportFbColNames = "";
        String finalSqlColsName = "";
        String[] columnIds = colmnIds.split(",");
        colNamesWithTotal.clear();
        for (int i = 0; i < columnIds.length; i++) {
            strCol = "";
            String formula;
            SqlCol1 = "";
            SqlCol2 = "";
            // Custom formulas
            ReportsAutomationCustomColumns colNames = new ReportsAutomationCustomColumns();
            automationCustomColumns = automationCustomColumnsDao.getObject(Long.parseLong(columnIds[i]));
            if (automationCustomColumns.getType() == 0 || automationCustomColumns.getType() == 1) {
                if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.COST_NAME)) {
                    reportFbColNames = reportFbColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                    currencyFlag = true;
                } else {
                    reportFbColNames = reportFbColNames + automationCustomColumns.getVariableName() + ",";
                }
            } else {
                formula = getCustomSqlColumnNameForTotals(automationCustomColumns);
                automationCustomColumns.setFormula(formula);
                if (automationCustomColumns.getFormula().contains(AutoReportConstants.REVENUE_NAME) && automationCustomColumns.getFormula().contains(AutoReportConstants.COST_NAME) && automationCustomColumns.getFormula().contains("/")) {
                    reportFbColNames = reportFbColNames + automationCustomColumns.getVariableName() + ",";
                } else if (automationCustomColumns.getFormula().contains(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getFormula().contains(AutoReportConstants.COST_NAME)) {
                    reportFbColNames = reportFbColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                    currencyFlag = true;
                } else if (automationCustomColumns.getFormula().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME) || automationCustomColumns.getFormula().equalsIgnoreCase(AutoReportConstants.COST_NAME)) {
                    reportFbColNames = reportFbColNames + automationCustomColumns.getVariableName() + " (" + currencySymbol + "),";
                    currencyFlag = true;
                } else {
                    reportFbColNames = reportFbColNames + automationCustomColumns.getVariableName() + ",";
                }
            }

            if (automationCustomColumns.getType() == 3) {

                if (fbAcc_id != 0) {

//                    if (accountDetails.get(j).getSearchEngineID() == CommonConstants.SE_FACEBOOK) {
                    if (strCol.equalsIgnoreCase("")) {
                        strCol = getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_FACEBOOK);
                    } else {
                        strCol = strCol + "," + getStandardSqlColumnName(AutoReportConstants.REPORTS_SOURCE_FACEBOOK);
                    }
//                    }
                }
                sqlCols.add(strCol);
            }

            colNames.setVariableName(automationCustomColumns.getVariableName());
            colNames.setFormula(getCustomSqlColumnNameForTotals(automationCustomColumns));
            colNames.setType(automationCustomColumns.getType());
            colNames.setColumnId(automationCustomColumns.getColumnId());
            if (currencyFlag) {
                colNames.setCurrencySymbol(currencySymbol);
            }
            colNamesWithTotal.add(colNames);
        }
        for (int j = 0; j < sqlCols.size(); j++) {
            finalSqlColsName = finalSqlColsName + sqlCols.get(j) + ",";
        }
        return finalSqlColsName;

    }

    private String getStandardSqlColumnName(int source) {
        String sqlColumnName = "";
        if (source == AutoReportConstants.REPORTS_SOURCE_GOOGLE) {

            if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                sqlColumnName = gorders;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                sqlColumnName = grevenue;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.TAX_NAME)) {
                sqlColumnName = gtax;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.SHIPPING_NAME)) {
                sqlColumnName = gshipping;
            } else {
                sqlColumnName = "g" + automationCustomColumns.getVariableName();
            }

        } else if (source == AutoReportConstants.REPORTS_SOURCE_MSN) {

            if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                sqlColumnName = morders;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                sqlColumnName = mrevenue;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.TAX_NAME)) {
                sqlColumnName = mtax;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.SHIPPING_NAME)) {
                sqlColumnName = mshipping;
            } else {
                sqlColumnName = "m" + automationCustomColumns.getVariableName();
            }

        } else if (source == AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI) {

            if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                sqlColumnName = yorders;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                sqlColumnName = yrevenue;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.TAX_NAME)) {
                sqlColumnName = ytax;
            } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.SHIPPING_NAME)) {
                sqlColumnName = yshipping;
            } else {
                sqlColumnName = "y" + automationCustomColumns.getVariableName();
            }

        } else if (source == AutoReportConstants.REPORTS_SOURCE_AMAZON) {
            if (amzAccType == 1) {
                if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    sqlColumnName = aorders;
                } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    sqlColumnName = arevenue;
                } else {
                    sqlColumnName = "a" + automationCustomColumns.getVariableName();
                }
            } else {
                if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    sqlColumnName = aorders + "," + horders;
                } else if (automationCustomColumns.getVariableName().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    sqlColumnName = arevenue + "," + hrevenue;
                } else {
                    sqlColumnName = "a" + automationCustomColumns.getVariableName() + "," + "h" + automationCustomColumns.getVariableName();
                }
            }
        } else if (source == AutoReportConstants.REPORTS_SOURCE_FACEBOOK) {
            sqlColumnName = "f" + automationCustomColumns.getVariableName();
        }
        return sqlColumnName;
    }

    private String getCustomSqlColumnNameForTotals(ReportsAutomationCustomColumns rprtAutoCustomColumInfo) {
        String sqlColumnName = "";
        if (rprtAutoCustomColumInfo.getType() == 3) {
            sqlColumnName = rprtAutoCustomColumInfo.getLeftOperand()
                    + rprtAutoCustomColumInfo.getOperator()
                    + rprtAutoCustomColumInfo.getRightOperand();
        } else {
            sqlColumnName = rprtAutoCustomColumInfo.getVariableName();
        }

        return sqlColumnName;
    }

    private String getCustomSqlColumnName(int source) {
        String sqlColumnName = "";

        switch (source) {
            case AutoReportConstants.REPORTS_SOURCE_GOOGLE:
                if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol1 = gorders;
                } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol1 = grevenue;
                } else {
                    SqlCol1 = "g" + automationCustomColumns.getLeftOperand();
                }
                if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol2 = gorders;
                } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol2 = grevenue;
                } else {
                    SqlCol2 = "g" + automationCustomColumns.getRightOperand();
                }
                sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                break;
            case AutoReportConstants.REPORTS_SOURCE_MSN:
                if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol1 = morders;
                } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol1 = mrevenue;
                } else {
                    SqlCol1 = "m" + automationCustomColumns.getLeftOperand();
                }
                if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol2 = morders;
                } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol2 = mrevenue;
                } else {
                    SqlCol2 = "m" + automationCustomColumns.getRightOperand();
                }
                sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                break;
            case AutoReportConstants.REPORTS_SOURCE_YAHOO_GEMINI:
                if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol1 = yorders;
                } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol1 = yrevenue;
                } else {
                    SqlCol1 = "y" + automationCustomColumns.getLeftOperand();
                }
                if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol2 = yorders;
                } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol2 = yrevenue;
                } else {
                    SqlCol2 = "y" + automationCustomColumns.getRightOperand();
                }
                sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                break;
            case AutoReportConstants.REPORTS_SOURCE_AMAZON:
                if (amzAccType == 1) {
                    if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                        SqlCol1 = aorders;
                    } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                        SqlCol1 = arevenue;
                    } else {
                        SqlCol1 = "a" + automationCustomColumns.getLeftOperand();
                    }
                    if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                        SqlCol2 = aorders;
                    } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                        SqlCol2 = arevenue;
                    } else {
                        SqlCol2 = "a" + automationCustomColumns.getRightOperand();
                    }
                    sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                } else {//for both SPA & HSA -AmazonTest
                    //For SPA amzCmpType
                    if (amzCmpType == CommonConstants.SPONSOREDPRODUCTS) {
                        if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                            SqlCol1 = aorders;
                        } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                            SqlCol1 = arevenue;
                        } else {
                            SqlCol1 = "a" + automationCustomColumns.getLeftOperand();
                        }
                        if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                            SqlCol2 = aorders;
                        } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                            SqlCol2 = arevenue;
                        } else {
                            SqlCol2 = "a" + automationCustomColumns.getRightOperand();
                        }
                        sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                    } else {
                        if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                            SqlCol1 = horders;
                        } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                            SqlCol1 = hrevenue;
                        } else {
                            SqlCol1 = "h" + automationCustomColumns.getLeftOperand();
                        }
                        if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                            SqlCol2 = horders;
                        } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                            SqlCol2 = hrevenue;
                        } else {
                            SqlCol2 = "h" + automationCustomColumns.getRightOperand();
                        }
                        sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                    }
                }
                break;
            case AutoReportConstants.REPORTS_SOURCE_FACEBOOK:
                if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol1 = forders;
                } else if (automationCustomColumns.getLeftOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol1 = fbrevenue;
                } else {
                    SqlCol1 = "f" + automationCustomColumns.getLeftOperand();
                }
                if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.ORDERS_NAME)) {
                    SqlCol2 = forders;
                } else if (automationCustomColumns.getRightOperand().equalsIgnoreCase(AutoReportConstants.REVENUE_NAME)) {
                    SqlCol2 = fbrevenue;
                } else {
                    SqlCol2 = "f" + automationCustomColumns.getRightOperand();
                }
                sqlColumnName = SqlCol1 + automationCustomColumns.getOperator() + SqlCol2;
                break;
            default:
                break;
        }
        return sqlColumnName;
    }

    public String getAmazonWeeklyPerformanceReport() {
        tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
        StringBuilder mthTllDateQuery = new StringBuilder();
        try {
            amzAcc_id = Long.parseLong(templateInfo.getAmzAccId());
            List<AccountDetails> accountInfo = accountDetailsDao.getAccountInfo(amzAcc_id);
            se_amzAccountId = accountInfo.get(0).getSeAccountId();
            amzAccType = accountInfo.get(0).getAmz_AccType();
            if (!isSigned) {
                cal = Calendar.getInstance();
                if (isCustomDownload()) { // for custom download
                    cal.add(Calendar.DATE, -1);
                    cal.set(Calendar.DAY_OF_WEEK, weekStart);
                    if (cal.getTime().after(Calendar.getInstance().getTime())) {
                        cal.add(Calendar.WEEK_OF_MONTH, -1);
                    }
                } else {
                    cal.add(Calendar.DATE, -2);
                    cal.set(Calendar.DAY_OF_WEEK, weekStart);
                }
                String day = String.valueOf(cal.get(Calendar.DATE));
                String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                String yearNo = String.valueOf(cal.get(Calendar.YEAR));
                String fromdate = yearNo + "-" + month + "-" + "01";
                String todate = yearNo + "-" + month + "-" + day;

                int monthNo = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                cal.set(Calendar.DATE, 1);
//                StringBuilder defMtdQry = new StringBuilder(); // MTD Query
                mthTllDateQuery.append("select revenue,cost,orders,id from (select ifnull(sum(REVENUE),0) as revenue,ifnull(sum(COST),0) as cost,ifnull(sum(CONVERSIONS),0) as orders,2 as id from Amazon_Report_CampaignStats where  SE_ACCOUNT_ID = ")
                        .append(se_amzAccountId).append(" and day between '").append(fromdate).append("' and '").append(todate).append("' ").append(" and year = ").append(year).append(" )amzMtd");
                LOGGER.info("mthTllDateQuery : " + mthTllDateQuery);
                autoReportsStatsCol = new AutoReportsStatsInfo();
                autoReportsStatsCol = autoReportDownloadDao.AmazonMTDObjects(mthTllDateQuery.toString(), templateInfo); //To set MTD data

                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.AMAZON_PERFORMANCE_SUMMARY_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            }
            getWeeklyQueries(tmpTablename, "508", AutoReportConstants.AMAZON_STANDARD_REPORT, false);
            getAmazonMergeQueries(tmpTablename);

            finalTotals = new ArrayList();
            colNamesWithTotal = new ArrayList<>();
            amzSqlColumnNames = getAmzSqlColumnNames();
            amzSqlColumnNames = amzSqlColumnNames.substring(0, amzSqlColumnNames.length() - 1);
            if (amzAccType == 1) {
                amzBasicTotalSqlColumnNames = "ifnull(aimpressions,0) as impressions ,ifnull(aclicks,0) as clicks,ifnull(acost,0) as cost,ifnull(" + arevenue + ",0) as revenue,ifnull(" + aorders + ",0) as orders";
                amzSqlColumnNames = amzSqlColumnNames + ",case aRevenue when 0 then 0 else (aCost/aRevenue) end as 'aCost/aRevenue *100' ";
            } else {
                amzBasicTotalSqlColumnNames = "(ifnull(aimpressions,0) + ifnull(himpressions,0)) as impressions,(ifnull(aclicks,0) + ifnull(hclicks,0)) as clicks,(ifnull(acost,0) + ifnull(hcost,0)) as cost,(ifnull(aRevenue,0) + ifnull(hRevenue,0)) as revenue,(ifnull(aOrders,0) + ifnull(hOrders,0)) as orders ";
                amzSqlColumnNames = amzSqlColumnNames + ",case aRevenue when 0 then 0 else (aCost/aRevenue) end as 'aCost/aRevenue *100',case hRevenue when 0 then 0 else (hCost/hRevenue) end as 'hCost/hRevenue *100' ";
            }
//            LOGGER.info("basicTotalSqlColumnNames :" + basicTotalSqlColumnNames); //To set data to "finalTotals" object
//            basicTotalSqlColumnNames = "ifnull(aimpressions,0) as impressions ,ifnull(aclicks,0) as clicks,ifnull(acost,0) as cost,ifnull(" + arevenue + ",0) as revenue,ifnull(" + aorders + ",0) as orders";
//            String basicTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by week_no  desc";
            String basicTotalsQuery = "select " + amzBasicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by week_no  desc";
            LOGGER.info("amzBasicTotalSqlColumnNames : " + amzBasicTotalSqlColumnNames);

            ReportsAutomationCustomColumns amzACOS = autoReportDownloadDao.getAmzAcosObject();
            amzACOS.setFormula(getCustomSqlColumnNameForTotals(amzACOS));
            colNamesWithTotal.add(amzACOS);
            finalTotals = autoReportDownloadDao.loadObjectsForTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);

//            amzSqlColumnNames = amzSqlColumnNames + ",case aRevenue when 0 then 0 else (aCost/aRevenue) end as 'aCost/aRevenue *100' ";
            LOGGER.info("amzSqlColumnNames :" + amzSqlColumnNames);// Add ACOS to Metrics
            finalStatsQuery = "select date_range," + amzSqlColumnNames + " from " + tmpTablename + " where yearoveryear = 0 order by week_no desc";
            LOGGER.info("Amazon Sem Weekly query>>>>>>>>>>" + finalStatsQuery); // final query to generate report
//            String newQuery = "select date_range, aImpressions,aClicks,acost,aOrders,aRevenue  from " + tmpTablename + " where yearoveryear = 0 order by week_no desc";

            reportColNames = reportColNames + "ACOS";
            if (!isSigned) {
                excelGenerator = setDataToExcelGenerator();
                excelGenerator.setReportColNames("Date Range," + reportColNames);//For Standard & Custom columns to be included
                excelGenerator.setReportName(AutoReportConstants.AMAZON_SEM_WEEKLY_REPORT);
                excelGenerator.setReportType(AutoReportConstants.AMAZON_REPORT_WEEKLY);
                excelGenerator.setAmzAccType(amzAccType);
                excelGenerator.setLoopCnt(weeklyLoopCnt - 2);
                excelGenerator.generateNewXLSXReport();
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            if (!isSigned) {
                LOGGER.info("drop tmp table " + tmpTablename);
                autoReportDownloadDao.dropTable(tmpTablename);
            }
        }
        return finalStatsQuery;
    }

    public void getAmazonMonthlyPerformanceReport() {

        tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
        try {
            amzAcc_id = Long.parseLong(templateInfo.getAmzAccId());
            List<AccountDetails> accountInfo = accountDetailsDao.getAccountInfo(amzAcc_id);
            se_amzAccountId = accountInfo.get(0).getSeAccountId();
            amzAccType = accountInfo.get(0).getAmz_AccType();
            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.AMAZON_PERFORMANCE_SUMMARY_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
            // Monthly Queries
            getMonthlyQueries(tmpTablename, "508", AutoReportConstants.AMAZON_STANDARD_REPORT, false);
            // Inserting into temp table
            getAmazonMergeQueries(tmpTablename);

            autoReportsStatsCol = new AutoReportsStatsInfo();
            finalTotals = new ArrayList();
            amzSqlColumnNames = getAmzSqlColumnNames();
            amzSqlColumnNames = amzSqlColumnNames.substring(0, amzSqlColumnNames.length() - 1);
            if (amzAccType == 1) {
                amzBasicTotalSqlColumnNames = "ifnull(aimpressions,0) as impressions ,ifnull(aclicks,0) as clicks,ifnull(acost,0) as cost,ifnull(" + arevenue + ",0) as revenue,ifnull(" + aorders + ",0) as orders";
                amzSqlColumnNames = amzSqlColumnNames + ",case aRevenue when 0 then 0 else (aCost/aRevenue) end as 'aCost/aRevenue *100' ";
            } else {
                amzBasicTotalSqlColumnNames = "(ifnull(aimpressions,0) + ifnull(himpressions,0)) as impressions,(ifnull(aclicks,0) + ifnull(hclicks,0)) as clicks,(ifnull(acost,0) + ifnull(hcost,0)) as cost,(ifnull(aRevenue,0) + ifnull(hRevenue,0)) as revenue,(ifnull(aOrders,0) + ifnull(hOrders,0)) as orders ";
                amzSqlColumnNames = amzSqlColumnNames + ",case aRevenue when 0 then 0 else (aCost/aRevenue) end as 'aCost/aRevenue *100',case hRevenue when 0 then 0 else (hCost/hRevenue) end as 'hCost/hRevenue *100' ";
            }
//                amzBasicTotalSqlColumnNames = "ifnull(aimpressions,0) as impressions ,ifnull(aclicks,0) as clicks,ifnull(acost,0) as cost,ifnull(" + arevenue + ",0) as revenue,ifnull(" + aorders + ",0) as orders";

            String basicTotalsQuery = "select " + amzBasicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by week_no  desc";
            LOGGER.info("amzBasicTotalSqlColumnNames : " + amzBasicTotalSqlColumnNames);

            ReportsAutomationCustomColumns amzACOS = autoReportDownloadDao.getAmzAcosObject();
            amzACOS.setFormula(getCustomSqlColumnNameForTotals(amzACOS));
            colNamesWithTotal.add(amzACOS);
            finalTotals = autoReportDownloadDao.loadObjectsForTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);

//                amzSqlColumnNames = amzSqlColumnNames + ",case aRevenue when 0 then 0 else (aCost/aRevenue) end as 'aCost/aRevenue *100' ";
            LOGGER.info("amzSqlColumnNames :" + amzSqlColumnNames);
            finalStatsQuery = "select date_range," + amzSqlColumnNames + " from " + tmpTablename + " where yearoveryear = 0 order by week_no desc";
            LOGGER.info("Amazon Sem Monthly query>>>>>>>>>>" + finalStatsQuery); // final query to generate report

            reportColNames = reportColNames + "ACOS";
            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setReportColNames("Date Range," + reportColNames);//For Standard & Custom columns to be included
            excelGenerator.setReportName(AutoReportConstants.AMAZON_SEM_MONTHLY_REPORT);
            excelGenerator.setReportType(AutoReportConstants.AMAZON_REPORT_MONTHLY);
            excelGenerator.setAmzAccType(amzAccType);
            excelGenerator.setLoopCnt(monthlyLoopCnt - 2);
            excelGenerator.setIsMonthlyReport(1);
            excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
    }

    public String getWeeklyPerformanceReport() {
        tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
        try {
            if (!isSigned) {
                StringBuilder mthTllDateQuery = new StringBuilder();
                cal = Calendar.getInstance();
                if (isCustomDownload()) { // for custom download
                    cal.add(Calendar.DATE, -1);
                    cal.set(Calendar.DAY_OF_WEEK, weekStart);
                    if (cal.getTime().after(Calendar.getInstance().getTime())) {
                        cal.add(Calendar.WEEK_OF_MONTH, -1);
                    }
                } else {
                    cal.add(Calendar.DATE, -2);
                    cal.set(Calendar.DAY_OF_WEEK, weekStart);
                }
                int monthNo = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                cal.set(Calendar.DATE, 1);
                StringBuilder defMtdQry = new StringBuilder();
                if (templateInfo.getRevenueType() == 1) {
                    defMtdQry.append("select revenue,cost,orders,id from ( ")
                            .append(" select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders,2 as id from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(gAcc_id).append(",").append(mAcc_id).append(",").append(yahAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ");
                } else if (templateInfo.getRevenueType() == 2) {
                    defMtdQry.append("select revenue,product_revenue,cost,orders,id from ( ")
                            .append(" select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(total_conv_value),0) as product_revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders,2 as id from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(gAcc_id).append(",").append(mAcc_id).append(",").append(yahAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ");
                }
                if (templateInfo.getTarget() != null) {
                    String accTar = templateInfo.getTarget();
                    String[] tarArr = accTar.split("-");
                    if (tarArr.length == 3) {
                        if (Integer.valueOf(tarArr[0]) == 1) {
                            //many per click
                            if (templateInfo.getRevenueType() == 1) {
                                mthTllDateQuery.append("select revenue,cost,orders,id from ( SELECT SUM(REVENUE) AS REVENUE,SUM(COST) AS COST,SUM(ORDERS) AS ORDERS,2 AS ID FROM ( ")
                                        .append(" select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(gAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ")
                                        .append(" union all select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(mAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ")
                                        .append(" union all select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(yahAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ) mtd1");
                            } else if (templateInfo.getRevenueType() == 2) {
                                mthTllDateQuery.append("select revenue,product_revenue,cost,orders,id from ( SELECT SUM(REVENUE) AS REVENUE,SUM(PRODUCT_REVENUE) AS PRODUCT_REVENUE,SUM(COST) AS COST,SUM(ORDERS) AS ORDERS,2 AS ID FROM ( ")
                                        .append(" select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(total_conv_value),0) as product_revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(gAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ")
                                        .append(" union all select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(total_conv_value),0) as product_revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(mAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ")
                                        .append(" union all select ifnull(sum(total_conv_value),0) as revenue,ifnull(sum(total_conv_value),0) as product_revenue,ifnull(sum(cost),0) as cost,ifnull(sum(conversions),0) as orders from NE_MONTHLY_SE_STATS_VIEW where   ACCOUNT_ID in ( ").append(yahAcc_id).append(") and is_month_till_date = 1 and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ) mtd1");
                            }
                        } else {
                            mthTllDateQuery.append(defMtdQry);
                        }
                    } else {
                        mthTllDateQuery.append(defMtdQry);
                    }
                }

                //   If GA is selected 
                if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 1) {
                    mthTllDateQuery.append(" union all ").append(" select ifnull(sum(ga_revenue),0) as revenue,0 as cost,ifnull(sum(ga_transactions),0) as orders,1 as id from NE_MONTHLY_GA_STATS_VIEW where    is_month_till_date = 1 and ACCOUNT_ID in ( ").append(gAcc_id).append(",").append(mAcc_id).append(",").append(yahAcc_id).append(")  and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ) mtd2");
                } else if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 2) {
                    mthTllDateQuery.append(" union all ").append(" select ifnull(sum(ga_revenue),0) as revenue, ifnull(sum(ga_product_revenue),0) as product_revenue,0 as cost,ifnull(sum(ga_transactions),0) as orders,1 as id from NE_MONTHLY_GA_STATS_VIEW where    is_month_till_date = 1 and ACCOUNT_ID in ( ").append(gAcc_id).append(",").append(mAcc_id).append(",").append(yahAcc_id).append(")  and month_no = ").append(monthNo).append(" and year = ").append(year).append(" ) mtd2");
                } else {
                    mthTllDateQuery.append(" ) mtd2 ");
                }
                LOGGER.info("month till date Auto Report SQl ........" + mthTllDateQuery.toString());

                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.PERFORMANCE_SUMMARY_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
                LOGGER.info("Month Till Date Query : " + mthTllDateQuery.toString());
                autoReportsStatsCol = new AutoReportsStatsInfo();
                autoReportsStatsCol = autoReportDownloadDao.loadMnthTillDateObjects(mthTllDateQuery.toString(), templateInfo);
            }
            getWeeklyQueries(tmpTablename, "504", AutoReportConstants.STANDARD_REPORT, false);
            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);

            LOGGER.info("basicTotalSqlColumnNames :" + basicTotalSqlColumnNames);
            String basicTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by week_no  desc";
            LOGGER.info("basicTotalsQuery :" + basicTotalsQuery);
            LOGGER.info("sqlColumnNames :" + sqlColumnNames);
            finalStatsQuery = "select date_range," + sqlColumnNames + " from " + tmpTablename + " where yearoveryear = 0 order by week_no desc";
            LOGGER.info("Sem Weekly query>>>>>>>>>>" + finalStatsQuery);
            finalTotals = new ArrayList();
            finalTotals = autoReportDownloadDao.loadObjectsForTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);

            if (!isSigned) {
                excelGenerator = setDataToExcelGenerator();
                if (yearOverYear == true) {
                    excelGenerator.setPrevYear(false);
                }
                excelGenerator.setReportColNames("Date Range," + reportColNames);
                excelGenerator.setReportName(AutoReportConstants.SEM_WEEKLY_REPORT);
                excelGenerator.setReportType(AutoReportConstants.PERFORMANCE_SUMMARY_REPORT_WEEKLY);
                excelGenerator.setLoopCnt(weeklyLoopCnt - 2);
                excelGenerator.generateNewXLSXReport();
                if (yearOverYear == true) {
                    excelGenerator.setYearOverYear(yearOverYear);
                    finalStatsQuery = "select date_range,week_no," + sqlColumnNames + ",yearoveryear from " + tmpTablename + "  order by week_no desc";//new code
                    LOGGER.info("Sem Weekly yoy query>>>>>>>>>>" + finalStatsQuery);
                    excelGenerator.setSql(finalStatsQuery);
                    excelGenerator.generateNewXLSXReport();
                }
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            if (!isSigned) {
                LOGGER.info("drop tmp table " + tmpTablename);
                autoReportDownloadDao.dropTable(tmpTablename);
            }
        }

        return finalStatsQuery;
    }

    private void getWeeklyQueries(String tmpTablename, String grpIds, int reportType, boolean isYoy) {

        seAccId = accountDetailsDao.getAccountSeId(gAcc_id);
        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }
        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }
        if (fbAcc_id != 0) {
            fbseAccId = accountDetailsDao.getAccountSeId(fbAcc_id);
        }
        gleseStatsTableName = "ne_autoreportsweeklysestats";
        glegaStatsTableName = "ne_autoreportsweeklygastats";

        msnseStatsTableName = "ne_autoreportsweeklybingsestats";
        msngaStatsTableName = "ne_autoreportsweeklybinggastat";

        yahseStatsTableName = "ne_autoreportsyahweeklysestats";
        yahgaStatsTableName = "ne_autoreportsyahweeklygastats";

        durCondition = "week_no";
        isMonthTillDate = "";

        amzQuery = new StringBuilder();
        amzSPQuery = new StringBuilder();
        amzHSAQuery = new StringBuilder();
        amzMergeQuery = new StringBuilder();
        amzSPMergeQuery = new StringBuilder();
        amzHSAMergeQuery = new StringBuilder();

        gleQuery = new StringBuilder();
        gaGleQuery = new StringBuilder();

        gleYOYQuery = new StringBuilder();
        gaGleYOYQuery = new StringBuilder();

        gleMergeQuery = new StringBuilder();
        gaGleMergeQuery = new StringBuilder();

        gleYOYMergeQuery = new StringBuilder();
        gaGleYOYMergeQuery = new StringBuilder();

        msnQuery = new StringBuilder();
        gaMsnQuery = new StringBuilder();

        msnYOYQuery = new StringBuilder();
        gaMsnYOYQuery = new StringBuilder();

        msnMergeQuery = new StringBuilder();
        gaMsnMergeQuery = new StringBuilder();

        msnYOYMergeQuery = new StringBuilder();
        gaMsnYOYMergeQuery = new StringBuilder();

        yahGemQuery = new StringBuilder();
        gaYahGemQuery = new StringBuilder();

        yahGemYOYQuery = new StringBuilder();
        gaYahGemYOYQuery = new StringBuilder();

        yahGemMergeQuery = new StringBuilder();
        gaYahGemMergeQuery = new StringBuilder();

        yahGemYOYMergeQuery = new StringBuilder();
        gaYahGemYOYMergeQuery = new StringBuilder();

        fbQuery = new StringBuilder();
        fbWowQuery = new StringBuilder();
        totalWowQuery = new StringBuilder();
        fbMergeQuery = new StringBuilder();
        fbMomMergeQuery = new StringBuilder();
        fbMergeQuery = new StringBuilder();
        fbWomMergeQuery = new StringBuilder();
        totalFbQuery = new StringBuilder();
        if (fbAcc_id != 0 && grpIds == "509") {
            getFacebookTmpTableSql(tmpTablename);
        } else if (grpIds == "503") {
            getTmpGroupWoWTableSql(tmpTablename);
        } else if ("508".equals(grpIds)) {
            getAmazonTmpTableSql(tmpTablename);
        } else {
            getTmpGroupTableSql(tmpTablename);// Query for creating tmptable
        }

        cal = Calendar.getInstance();
        fcalYOY = Calendar.getInstance();
        fcalYOY.add(Calendar.DATE, -(fcalYOY.getActualMaximum(Calendar.DAY_OF_YEAR) - 1));//For getting previous year calendar (To resolve issue when year changes)
        if (isCustomDownload()) { // for custom download
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.DAY_OF_WEEK, weekStart);

            fcalYOY.add(Calendar.DATE, -1);
            fcalYOY.set(Calendar.DAY_OF_WEEK, weekStart);

            if (cal.getTime().after(Calendar.getInstance().getTime())) {
                cal.add(Calendar.WEEK_OF_MONTH, -1);
                fcalYOY.add(Calendar.WEEK_OF_MONTH, -1);

            }
        } else {
            cal.add(Calendar.DATE, -2);
            cal.set(Calendar.DAY_OF_WEEK, weekStart);
            fcalYOY.add(Calendar.DATE, -2);
            fcalYOY.set(Calendar.DAY_OF_WEEK, weekStart);
        }
        sqlQuery = new StringBuilder();

        int loopCnt = 0;
        if (reportType == CommonConstants.CAMPAIGN || reportType == CommonConstants.ADGROUP
                || reportType == AutoReportConstants.CONVERTING_KEYWORD || reportType == AutoReportConstants.FACEBOOK_REPORT_WEEKLY) {
            loopCnt = 2;
        } else {
            loopCnt = weeklyLoopCnt;
        }

        if (isYoy) {
            loopCnt = cal.get(Calendar.WEEK_OF_YEAR);
            if (weekStart == 7) {
                loopCnt = loopCnt + 1;
            }
            if (loopCnt == 1) {
                loopCnt = 53;
            }
        }
        for (int i = 1; i < loopCnt; i++) {
            if (i != 1) {
                cal.add(Calendar.DATE, -1);
                fcalYOY.add(Calendar.DATE, -1);
            }

            cal.add(Calendar.DATE, -6);
            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
            fdate = cal.get(Calendar.DATE);
            fyear = cal.get(Calendar.YEAR);
            fmonthNo = cal.get(Calendar.MONTH);
            fmonthName = CommonFunctions.getMonthName(fmonthNo);
            fcalYOY.add(Calendar.DATE, -6);

            yoyfdate = fcalYOY.get(Calendar.DATE);
            yoyfyear = fcalYOY.get(Calendar.YEAR);
            yoyfmonthNo = fcalYOY.get(Calendar.MONTH);
            yoyfmonthName = CommonFunctions.getMonthName(yoyfmonthNo);

//            fromDate = new DateConverter(cal).getOracleDateForm();
            if (fbAcc_id != 0) {
                fbFromDate = (simpleDateFormat.format(cal.getTime()));
            }
            fromDate = new DateConverter(cal).getSQLDateForm1();
            yoyFromDate = new DateConverter(fcalYOY).getOracleDateForm();

            cal.add(Calendar.DATE, 6);
            fcalYOY.add(Calendar.DATE, 6);

            tdate = cal.get(Calendar.DATE);
            tyear = cal.get(Calendar.YEAR);
            tmonthNo = cal.get(Calendar.MONTH);
            tmonthName = CommonFunctions.getMonthName(tmonthNo);

            yoytdate = fcalYOY.get(Calendar.DATE);
            yoytyear = fcalYOY.get(Calendar.YEAR);
            yoytmonthNo = fcalYOY.get(Calendar.MONTH);
            yoytmonthName = CommonFunctions.getMonthName(yoytmonthNo);

//            toDate = new DateConverter(cal).getOracleDateForm();
            if (fbAcc_id != 0) {
                fbToDate = (simpleDateFormat.format(cal.getTime()));
            }
            toDate = new DateConverter(cal).getSQLDateForm1();
            yoyToDate = new DateConverter(fcalYOY).getOracleDateForm();
            dateRange = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
            yoyDateRange = yoyfmonthName + " " + yoyfdate + "," + " " + yoyfyear + "-" + yoytmonthName + " " + yoytdate + "," + " " + yoytyear;

            cal.add(Calendar.DATE, -6);
            fcalYOY.add(Calendar.DATE, -6);
            if (i == 1) {
                reportingDate = dateRange;
            }
            durNo = weekOfYear;
            year = tyear;
            int wkNo = 0;
            if (isYoy) {
                wkNo = durNo;
            } else {
                wkNo = i;
            }
            if (reportType == AutoReportConstants.STANDARD_REPORT) {
                getGroupMonthlyOrWeeklyQueries(i, grpIds, reportType);
            } else if (isCustom == false && reportType == AutoReportConstants.GROUP_REPORT) {
                getGroupMonthlyOrWeeklyQueries(i, grpIds, reportType);
            } else if (reportType == AutoReportConstants.GROUP_TREND_REPORT) {
                getTrendGrpMonthlyOrWeeklyQueries(wkNo, grpIds);
            } else if (isCustom == true && reportType == AutoReportConstants.GROUP_REPORT) {
                getCustomGroupMonthlyOrWeeklyQueries(i, grpIds, reportType);
            } else if (reportType == AutoReportConstants.AMAZON_STANDARD_REPORT) {
                getAmazonWeeklyOrMonthlyQueries(i, grpIds);
            } else if (reportType == AutoReportConstants.FACEBOOK_REPORT_WEEKLY) {
                getFacebookWeeklyOrMonthlyQueries(i, grpIds, "weekly");
            }

        }
    }

    public void getAmazonTmpTableSql(String tmpTablename) {
        createTable = new StringBuilder();
        createTable.append(" create table ").append(tmpTablename).append("( week varchar(100),date_range varchar(100), week_no bigint(6),YEAR bigint(5),group_name varchar(100), yearoveryear tinyint(2),")
                .append("account_id bigint(38),campaign_name varchar(200), se_campaign_id bigint(38),")
                .append("aIMPRESSIONS bigint(38), aCLICKS  bigint(38),  aCOST decimal(38,2),  aORDERS bigint(38),aREVENUE decimal(38,2), ")
                .append("hIMPRESSIONS bigint(38), hCLICKS  bigint(38),  hCOST decimal(38,2),  hORDERS bigint(38),hREVENUE decimal(38,2) ")
                .append(" )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ");
        tablecreated = autoReportDownloadDao.executeQuery(createTable.toString());
    }

    public void getFacebookTmpTableSql(String tmpTablename) {
        createTable = new StringBuilder();
        createTable.append(" create table ").append(tmpTablename).append("(week varchar(100),date_range varchar(100), week_no bigint(6), month_no  varchar(100),YEAR bigint(5),group_name varchar(100), yearoveryear tinyint(2),")
                .append("account_id bigint(38),campaign_name varchar(200),campaign_objective varchar(200), campaign_id bigint(38),")
                .append(" results bigint(20),reach bigint(20) ,fIMPRESSIONS bigint(38), fCLICKS  bigint(38),  fCOST decimal(38,2) , costperpurchase decimal(38,2),page_likes bigint(38),post_engagement bigint(38),  rbyc decimal(19,2),  fORDERS bigint(38),fREVENUE decimal(38,2) ,gafORDERS bigint(38),gafREVENUE decimal(38,2),gataxfREVENUE decimal(38,2),"
                        + "gaShippingfREVENUE decimal(38,2),totalfbrevenue decimal(19,2),totalfbcost decimal(19,2),totalfborders bigint(5),fbid tinyint(2) ,totalfbgarevenue  decimal(19,2) , totalfbgaorders bigint(5) ,"
                        + " fbgaid tinyint(2) , IS_MOM tinyint(2) , curfmonth_no varchar(100) )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        tablecreated = autoReportDownloadDao.executeQuery(createTable.toString());
    }

    public void getTmpGroupTableSql(String tmpTablename) {
        createTable = new StringBuilder();

        createTable.append(" create table ").append(tmpTablename).append(" (week varchar(100),YEAR bigint(5),group_name varchar(100), account_id bigint(38),")
                .append("campaign_name varchar(200), se_campaign_id bigint(38),adgroup_name varchar(255),se_adgroup_id bigint(38), ")
                .append("date_range  varchar(100),  week_no bigint(6),  ")
                .append("gIMPRESSIONS bigint(38), gCLICKS  bigint(38),  gCOST decimal(38,2),  gorders bigint(38),")
                .append("grevenue decimal(38,2), gavg_pos decimal(12,2),gTAX decimal(38,2) , gSHIPPING decimal(38,2) ,")
                .append("mIMPRESSIONS bigint(38), mCLICKS  bigint(38),  mCOST decimal(38,2),  morders bigint(38),")
                .append("mrevenue decimal(38,2), mavg_pos decimal(12,2),mTAX decimal(38,2) , mSHIPPING decimal(38,2) ,")
                .append("yIMPRESSIONS bigint(38), yCLICKS bigint(38),yCOST decimal(38,2), yorders bigint(38), ")
                .append(" yrevenue decimal(38,2), yavg_pos decimal(12,2) ,")
                .append("gaIMPRESSIONS bigint(38),  gaCLICKS bigint(38), gaCOST decimal(38,2), gagleorders bigint(38),gaglerevenue decimal(38,2),")
                .append("gaglePRevenue decimal(38,2), gagletax decimal(38,2), gagleshipping decimal(38,2), ")
                .append("gamsnorders bigint(38),gamsnrevenue decimal(38,2),")
                .append("gamsnPRevenue decimal(38,2), gamsntax decimal(38,2), gamsnshipping decimal(38,2), ")
                .append(" gayahorders bigint(38),").append(" gayahrevenue decimal(38,2), gayahPRevenue decimal(38,2),").append(" gayahtax decimal(38,2),").append(" gayahshipping decimal(38,2),")
                .append("KEYWORD_NAME VARCHAR(512),QUALITY_SCORE tinyint(2),MATCH_TYPE varchar(50),")
                .append("SE_KEYWORD_ID   bigint(38),yearoveryear tinyint(2) default 0 ,")
                .append("Constraint keyword UNIQUE(account_id,campaign_name (40), \n"
                        + "keyword_name (150)))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");

        tablecreated = autoReportDownloadDao.executeQuery(createTable.toString());
    }

    public void getTmpGroupWoWTableSql(String tmpTablename) {
        createTable = new StringBuilder();

        createTable.append(" create table ").append(tmpTablename).append(" (week varchar(100),YEAR bigint(5),group_name varchar(100), account_id bigint(38),")
                .append("campaign_name varchar(200), se_campaign_id bigint(38),adgroup_name varchar(255),se_adgroup_id bigint(38), ")
                .append("date_range  varchar(100),  week_no bigint(6),  ")
                .append("gIMPRESSIONS bigint(38), gCLICKS  bigint(38),  gCOST decimal(38,2),  gorders bigint(38),")
                .append("grevenue decimal(38,2), gavg_pos decimal(12,2),gTAX decimal(38,2) , gSHIPPING decimal(38,2) ,")
                .append("mIMPRESSIONS bigint(38), mCLICKS  bigint(38),  mCOST decimal(38,2),  morders bigint(38),")
                .append("mrevenue decimal(38,2), mavg_pos decimal(12,2),mTAX decimal(38,2) , mSHIPPING decimal(38,2) ,")
                .append("yIMPRESSIONS bigint(38), yCLICKS bigint(38),yCOST decimal(38,2), yorders bigint(38), ")
                .append(" yrevenue decimal(38,2), yavg_pos decimal(12,2) ,")
                .append("gaIMPRESSIONS bigint(38),  gaCLICKS bigint(38), gaCOST decimal(38,2), gagleorders bigint(38),gaglerevenue decimal(38,2),")
                .append("gaglePRevenue decimal(38,2), gagletax decimal(38,2), gagleshipping decimal(38,2), ")
                .append("gamsnorders bigint(38),gamsnrevenue decimal(38,2),")
                .append("gamsnPRevenue decimal(38,2), gamsntax decimal(38,2), gamsnshipping decimal(38,2), ")
                .append(" gayahorders bigint(38),").append(" gayahrevenue decimal(38,2), gayahPRevenue decimal(38,2),").append(" gayahtax decimal(38,2),").append(" gayahshipping decimal(38,2),")
                .append("KEYWORD_NAME VARCHAR(512),QUALITY_SCORE tinyint(2),MATCH_TYPE varchar(50),")
                .append("SE_KEYWORD_ID   bigint(38),yearoveryear tinyint(2) default 0 ,")
                .append("Constraint keyword UNIQUE(campaign_name (40),week_no,date_range,year))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ");

        tablecreated = autoReportDownloadDao.executeQuery(createTable.toString());
    }

    private void getAmazonWeeklyOrMonthlyQueries(int i, String grpIds) {
        StringBuilder durCondStr = new StringBuilder();
        if (amzAccType == 1) {
            if ("month_no".equals(durCondition)) {
//            durCondStr.append(" year = 2017 and  DAY between '2017-10-22' and '2017-10-23' ");
                durCondStr.append(" year = ").append(year).append(" and ").append(" month_no = ").append(fmonthNo);
                amzQuery.append("  select ").append(i).append(" as week_no,Week,year,Date_Range,sum(IMPRESSIONS) as aimpressions, ")
                        .append(" sum(clicks) as aclicks,sum(cost) as acost, sum(CONVERSIONS) as aOrders,sum(REVENUE) as aRevenue,group_name,0 as yearoveryear  FROM ")
                        .append("( select IMPRESSIONS,CLICKS,COST,CONVERSIONS,REVENUE,group_name,Week,year, Date_Range,b1.CAMPAIGN_ID from ")
                        .append("( select 'RW").append(i).append(" ' as Week,year,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                        .append(" sum(COST) as COST,sum(CONVERSIONS) as CONVERSIONS,sum(REVENUE) as REVENUE, CAMPAIGN_ID from ").append(amzStatsTableName).append(" where ")
                        .append(" SE_ACCOUNT_ID = ").append(se_amzAccountId).append(" and ").append(durCondStr).append("  group by CAMPAIGN_ID ) b1 join ")
                        .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append(" and group_id = ").append(grpIds).append(" and account_id = ").append(amzAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID) union all ")
                        .append(" (select 0 as  impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as REVENUE ,group_name, ")
                        .append(" 'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                        .append(dateRange).append("' as Date_Range , 0 as CAMPAIGN_ID  from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ))  k group by Week, year, Date_Range, GROUP_NAME  union all ");
            } else {
                durCondStr.append(" DAY between '").append(fromDate).append("' and '").append(toDate).append("' ");
                amzQuery.append("  select ").append(i).append(" as week_no,Week,Date_Range,sum(IMPRESSIONS) as aimpressions, ")
                        .append(" sum(clicks) as aclicks,sum(cost) as acost, sum(CONVERSIONS) as aOrders,sum(REVENUE) as aRevenue,group_name,0 as yearoveryear  FROM ")
                        .append("( select IMPRESSIONS,CLICKS,COST,CONVERSIONS,REVENUE,group_name,Week, Date_Range,b1.CAMPAIGN_ID from ")
                        .append("( select 'RW").append(i).append(" ' as Week,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                        .append(" sum(COST) as COST,sum(CONVERSIONS) as CONVERSIONS,sum(REVENUE) as REVENUE, CAMPAIGN_ID from ").append(amzStatsTableName).append(" where ")
                        .append(" SE_ACCOUNT_ID = ").append(se_amzAccountId).append(" and ").append(durCondStr).append("  group by CAMPAIGN_ID ) b1 join ")
                        .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append(" and group_id = ").append(grpIds).append(" and account_id = ").append(amzAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID) union all ")
                        .append(" (select 0 as  impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as REVENUE ,group_name, ")
                        .append(" 'RW").append(i).append(" ' as Week,").append(" '")
                        .append(dateRange).append("' as Date_Range , 0 as CAMPAIGN_ID  from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ))  k group by Week, Date_Range, GROUP_NAME  union all ");
            }
        } else {
            if ("month_no".equals(durCondition)) {
                durCondStr.append(" year = ").append(year).append(" and ").append(" month_no = ").append(fmonthNo);
                amzSPQuery.append("  select ").append(i).append(" as week_no,Week,year,Date_Range,sum(IMPRESSIONS) as aimpressions, ")
                        .append(" sum(clicks) as aclicks,sum(cost) as acost, sum(CONVERSIONS) as aOrders,sum(REVENUE) as aRevenue,group_name,0 as yearoveryear  FROM ")
                        .append("( select IMPRESSIONS,CLICKS,COST,CONVERSIONS,REVENUE,group_name,Week,year, Date_Range,b1.CAMPAIGN_ID from ")
                        .append("( select 'RW").append(i).append(" ' as Week,year,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                        .append(" sum(COST) as COST,sum(CONVERSIONS) as CONVERSIONS,sum(REVENUE) as REVENUE, CAMPAIGN_ID from ").append(amzStatsTableName).append(" where ")
                        .append(" SE_ACCOUNT_ID = ").append(se_amzAccountId).append(" and campaign_type = 1 and ").append(durCondStr).append("  group by CAMPAIGN_ID ) b1 join ")
                        .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append(" and group_id = ").append(grpIds).append(" and account_id = ").append(amzAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID) union all ")
                        .append(" (select 0 as  impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as REVENUE ,group_name, ")
                        .append(" 'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                        .append(dateRange).append("' as Date_Range , 0 as CAMPAIGN_ID  from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ))  k group by Week, year, Date_Range, GROUP_NAME  union all ");

                amzHSAQuery.append("  select ").append(i).append(" as week_no,Week,year,Date_Range,sum(IMPRESSIONS) as himpressions, ")
                        .append(" sum(clicks) as hclicks,sum(cost) as hcost, sum(CONVERSIONS) as hOrders,sum(REVENUE) as hRevenue,group_name,0 as yearoveryear  FROM ")
                        .append("( select IMPRESSIONS,CLICKS,COST,CONVERSIONS,REVENUE,group_name,Week,year, Date_Range,b1.CAMPAIGN_ID from ")
                        .append("( select 'RW").append(i).append(" ' as Week,year,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                        .append(" sum(COST) as COST,sum(CONVERSIONS) as CONVERSIONS,sum(REVENUE) as REVENUE, CAMPAIGN_ID from ").append(amzStatsTableName).append(" where ")
                        .append(" SE_ACCOUNT_ID = ").append(se_amzAccountId).append(" and campaign_type = 2 and ").append(durCondStr).append("  group by CAMPAIGN_ID ) b1 join ")
                        .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append(" and group_id = ").append(grpIds).append(" and account_id = ").append(amzAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID) union all ")
                        .append(" (select 0 as  impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as REVENUE ,group_name, ")
                        .append(" 'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                        .append(dateRange).append("' as Date_Range , 0 as CAMPAIGN_ID  from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ))  k group by Week, year, Date_Range, GROUP_NAME  union all ");
            } else {
                durCondStr.append(" DAY between '").append(fromDate).append("' and '").append(toDate).append("' ");
                amzSPQuery.append("  select ").append(i).append(" as week_no,Week,Date_Range,sum(IMPRESSIONS) as aimpressions, ")
                        .append(" sum(clicks) as aclicks,sum(cost) as acost, sum(CONVERSIONS) as aOrders,sum(REVENUE) as aRevenue,group_name,0 as yearoveryear  FROM ")
                        .append("( select IMPRESSIONS,CLICKS,COST,CONVERSIONS,REVENUE,group_name,Week, Date_Range,b1.CAMPAIGN_ID from ")
                        .append("( select 'RW").append(i).append(" ' as Week,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                        .append(" sum(COST) as COST,sum(CONVERSIONS) as CONVERSIONS,sum(REVENUE) as REVENUE, CAMPAIGN_ID from ").append(amzStatsTableName).append(" where ")
                        .append(" SE_ACCOUNT_ID = ").append(se_amzAccountId).append(" and campaign_type = 1 and ").append(durCondStr).append("  group by CAMPAIGN_ID ) b1 join ")
                        .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append(" and group_id = ").append(grpIds).append(" and account_id = ").append(amzAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID) union all ")
                        .append(" (select 0 as  impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as REVENUE ,group_name, ")
                        .append(" 'RW").append(i).append(" ' as Week,").append(" '")
                        .append(dateRange).append("' as Date_Range , 0 as CAMPAIGN_ID  from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ))  k group by Week, Date_Range, GROUP_NAME  union all ");

                amzHSAQuery.append("  select ").append(i).append(" as week_no,Week,Date_Range,sum(IMPRESSIONS) as himpressions, ")
                        .append(" sum(clicks) as hclicks,sum(cost) as hcost, sum(CONVERSIONS) as hOrders,sum(REVENUE) as hRevenue,group_name,0 as yearoveryear  FROM ")
                        .append("( select IMPRESSIONS,CLICKS,COST,CONVERSIONS,REVENUE,group_name,Week, Date_Range,b1.CAMPAIGN_ID from ")
                        .append("( select 'RW").append(i).append(" ' as Week,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                        .append(" sum(COST) as COST,sum(CONVERSIONS) as CONVERSIONS,sum(REVENUE) as REVENUE, CAMPAIGN_ID from ").append(amzStatsTableName).append(" where ")
                        .append(" SE_ACCOUNT_ID = ").append(se_amzAccountId).append(" and campaign_type = 2 and ").append(durCondStr).append("  group by CAMPAIGN_ID ) b1 join ")
                        .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append(" and group_id = ").append(grpIds).append(" and account_id = ").append(amzAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID) union all ")
                        .append(" (select 0 as  impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as REVENUE ,group_name, ")
                        .append(" 'RW").append(i).append(" ' as Week,").append(" '")
                        .append(dateRange).append("' as Date_Range , 0 as CAMPAIGN_ID  from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ))  k group by Week, Date_Range, GROUP_NAME  union all ");
            }
        }
    }

    private void getGroupMonthlyOrWeeklyQueries(int i, String grpIds, int reportType) {
        if (reportType == -20) {
            gleQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as gimpressions, sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as ")
                    .append(" gRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS gavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append("  ").append(" client_id =").append(clientId)
                    .append(" and ").append("account_id = ").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo)
                    .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,reporting_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and reporting_id in (").append(grpIds).append(")");
            String[] grpIdStr = grpIds.split(",");
            for (int j = 0; j < grpIdStr.length; j++) {
                gleQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as reporting_id,").append(" 2 as component_level,adgroup_id from adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and reporting_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(gAcc_id).append(" )");

            }

            gleQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.reporting_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                    .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

            gleYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as gimpressions, sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as ")
                    .append(" gRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS gavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append("  ").append(" client_id =").append(clientId)
                    .append(" and ").append("account_id = ").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo + 1)
                    .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,reporting_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and reporting_id in (").append(grpIds).append(")");
            String[] grpIdStr1 = grpIds.split(",");
            for (int j = 0; j < grpIdStr1.length; j++) {
                gleYOYQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id,")
                        .append(grpIdStr1[j]).append(" as reporting_id,").append(" 2 as component_level,adgroup_id from adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and reporting_id in(").append(grpIdStr1[j]).append(")").append(" and account_id= ").append(gAcc_id).append(" )");

            }

            gleYOYQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.reporting_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                    .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
//      Bing Queries Starts here 
            msnQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as mimpressions, sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as ")
                    .append(" mRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS mavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(msnseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(msnseAccId).append(" and ").append(durCondition).append("=").append(durNo)
                    .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,reporting_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and reporting_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
            for (int j = 0; j < grpIdStr.length; j++) {
                msnQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as reporting_id,").append(" 2 as component_level,adgroup_id from bing_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and reporting_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(mAcc_id).append(" )");
            }
            msnQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.reporting_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                    .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
            msnYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as mimpressions, sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as ")
                    .append(" mRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS mavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(msnseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(msnseAccId).append(" and ").append(durCondition).append("=").append(durNo + 1)
                    .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,reporting_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and reporting_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
            for (int j = 0; j < grpIdStr.length; j++) {
                msnYOYQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as reporting_id,").append(" 2 as component_level,adgroup_id from bing_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and reporting_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(mAcc_id).append(" )");

            }

            msnYOYQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.reporting_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                    .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

//      YahooGemini Queries Starts here 
            yahGemQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as yimpressions, sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as ")
                    .append(" yRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS yavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(yahseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(yahseAccId).append(" and ").append(durCondition).append("=").append(durNo)
                    .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,reporting_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and reporting_id in (").append(grpIds).append(")");
            for (int j = 0; j < grpIdStr.length; j++) {
                yahGemQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as reporting_id,").append(" 2 as component_level,adgroup_id from yahgemini_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and reporting_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(yahAcc_id).append(" )");
            }
            yahGemQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.reporting_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                    .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

            yahGemYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as yimpressions, sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as ")
                    .append(" yRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS yavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(yahseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(yahseAccId).append(" and ").append(durCondition).append("=").append(durNo + 1)
                    .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,reporting_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and reporting_id in (").append(grpIds).append(")");
            for (int j = 0; j < grpIdStr.length; j++) {
                yahGemYOYQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as reporting_id,").append(" 2 as component_level,adgroup_id from yahgemini_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and reporting_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(yahAcc_id).append(" )");

            }

            yahGemYOYQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.reporting_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                    .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
        } else {
            gleQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as gimpressions, sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as ")
                    .append(" gRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS gavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append("  ").append(" client_id =").append(clientId)
                    .append(" and ").append("account_id = ").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo)
                    .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(")");
            String[] grpIdStr = grpIds.split(",");
            for (int j = 0; j < grpIdStr.length; j++) {
                gleQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and group_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(gAcc_id).append(" )");

            }

            gleQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                    .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

            gleYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as gimpressions, sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as ")
                    .append(" gRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS gavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append("  ").append(" client_id =").append(clientId)
                    .append(" and ").append("account_id = ").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo + 1)
                    .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(")");
            String[] grpIdStr1 = grpIds.split(",");
            for (int j = 0; j < grpIdStr1.length; j++) {
                gleYOYQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id,")
                        .append(grpIdStr1[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and group_id in(").append(grpIdStr1[j]).append(")").append(" and account_id= ").append(gAcc_id).append(" )");

            }

            gleYOYQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                    .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
//      Bing Queries Starts here 
            msnQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as mimpressions, sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as ")
                    .append(" mRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS mavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(msnseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(msnseAccId).append(" and ").append(durCondition).append("=").append(durNo)
                    .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
            for (int j = 0; j < grpIdStr.length; j++) {
                msnQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from bing_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and group_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(mAcc_id).append(" )");
            }
            msnQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                    .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
            msnYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as mimpressions, sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as ")
                    .append(" mRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS mavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(msnseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(msnseAccId).append(" and ").append(durCondition).append("=").append(durNo + 1)
                    .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
            for (int j = 0; j < grpIdStr.length; j++) {
                msnYOYQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from bing_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and group_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(mAcc_id).append(" )");

            }

            msnYOYQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                    .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

//      YahooGemini Queries Starts here 
            yahGemQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as yimpressions, sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as ")
                    .append(" yRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS yavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(yahseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(yahseAccId).append(" and ").append(durCondition).append("=").append(durNo)
                    .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(")");
            for (int j = 0; j < grpIdStr.length; j++) {
                yahGemQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from yahgemini_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and group_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(yahAcc_id).append(" )");
            }
            yahGemQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                    .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

            yahGemYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as yimpressions, sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as ")
                    .append(" yRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS yavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                    .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                    .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                    .append(" client_id ,adgroup_name ,adgroup_id   from ").append(yahseStatsTableName).append(" where ")
                    .append(isMonthTillDate).append(" level_of_detail = 2 ")
                    .append(" and ").append("se_account_id = ").append(yahseAccId).append(" and ").append(durCondition).append("=").append(durNo + 1)
                    .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                    .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(")");
            for (int j = 0; j < grpIdStr.length; j++) {
                yahGemYOYQuery.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id,")
                        .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from yahgemini_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                        .append(" (select campaign_id from reports_automation_groups where component_level=1 and group_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(yahAcc_id).append(" )");

            }

            yahGemYOYQuery.append(") grps ").append(" join ")
                    .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                    .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                    .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                    .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                    .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                    .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
        }
        if (templateInfo.getGoogleAnalytics() != 0) {

            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {

                if (reportType == AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 1) {

                    gaGleQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gagleRevenue) as gagleRevenue,group_name,0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id,se_campaign_id AS campaign_id from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  and adgroup_name is not null  group by se_campaign_id,year,account_id ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and  grpnames.campaign_id = b1.campaign_id )  group by  week,year,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" 'SEM Weekly' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            //                            .append(year).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id,year,account_id ")
                            .append(year).append(" and (se_campaign_id = 0 or se_adgroup_id = 0)  group by se_campaign_id,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                    gaGleYOYQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gagleRevenue) as gagleRevenue,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id,se_campaign_id AS campaign_id  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  and adgroup_name is not null  group by se_campaign_id,year,account_id ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and grpnames.campaign_id = b1.campaign_id)  group by  week,year,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" 'SEM Weekly' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            //                            .append(year - 1).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id,year,account_id ")
                            .append(year - 1).append(" and (se_campaign_id = 0 or se_adgroup_id = 0)  group by se_campaign_id,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                } else if (reportType == AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 2) {
                    gaGleQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gaglePRevenue) as gaglePRevenue,sum(gagleTax) as gagleTax, sum(gagleShipping) as gagleShipping,group_name,0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax, sum(GA_SHIPPING) as gagleShipping,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING")
                            .append(" , account_id,se_campaign_id AS campaign_id  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  and adgroup_name is not null  group by  se_campaign_id ,year,account_id ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and grpnames.campaign_id = b1.campaign_id ) group by  week,year,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" 'SEM Weekly' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            //                            .append(year).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id ,year,account_id ")
                            .append(year).append(" and (se_campaign_id = 0 or se_adgroup_id = 0)  group by se_campaign_id ,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                    gaGleYOYQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gaglePRevenue) as gaglePRevenue,sum(gagleTax) as gagleTax, sum(gagleShipping) as gagleShipping,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax, sum(GA_SHIPPING) as gagleShipping,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING")
                            .append(" , account_id,se_campaign_id AS campaign_id  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  and adgroup_name is not null  group by  se_campaign_id ,year,account_id ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and grpnames.campaign_id = b1.campaign_id ) group by  week,year,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" 'SEM Weekly' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            //                            .append(year - 1).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id ,year,account_id ")
                            .append(year - 1).append(" and (se_campaign_id = 0 or se_adgroup_id = 0)  group by se_campaign_id ,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                } else if (reportType != AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 1) {

                    gaGleQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) k group by  week,year,Date_Range,group_name union all ");

                    gaGleYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" ( select * from campaign_structure where  ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) )) h group by  week,year,Date_Range,group_name union all ");

                } else if (reportType != AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 2) {
                    gaGleQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) m group by  week,year,Date_Range,group_name union all ");

                    gaGleYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue, sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,group_name ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) ))  j group by  week,year,Date_Range,group_name union all ");
                }
            }
//            Bing Analytics Queries Starts here 
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {

                if (reportType == AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 1) {
                    gaMsnQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnOrders) as gamsnOrders,")
                            .append(" sum(gamsnRevenue) as gamsnRevenue,group_name,0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id, replace(campaign_name,' ','_') as campaign_name from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append(" group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and  upper(grpnames.campaign_name) = upper(b1.campaign_name) )  group by  week,year,Date_Range,group_name ")
                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" 'SEM Weekly' as  group_name from ").append(msngaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append(" client_id = ").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append(" and (CAMPAIGN_NAME = '(not set)')  group by CAMPAIGN_NAME, year, account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                    gaMsnYOYQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnOrders) as gamsnOrders,")
                            .append(" sum(gamsnRevenue) as gamsnRevenue,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id, replace(campaign_name,' ','_') as campaign_name from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("   group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and upper(grpnames.campaign_name) = upper(b1.campaign_name) )  group by  week,year,Date_Range,group_name")
                            .append(" union all  select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" 'SEM Weekly' as  group_name from ").append(msngaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append(" client_id = ").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year - 1).append(" and (CAMPAIGN_NAME = '(not set)')  group by CAMPAIGN_NAME,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                } else if (reportType == AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 2) {
                    gaMsnQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnOrders) as gamsnOrders,")
                            .append(" sum(gamsnPRevenue) as gamsnPRevenue,sum(gamsnTax) as gamsnTax, sum(gamsnShipping) as gamsnShipping,group_name,0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax, sum(GA_SHIPPING) as gamsnShipping,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING")
                            .append(" , account_id, replace(campaign_name,' ','_') as campaign_name  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  and adgroup_name is not null  group by  campaign_name ,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and upper(grpnames.campaign_name) = upper(b1.campaign_name) ) group by  week,year,Date_Range,group_name ")
                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" 'SEM Weekly' as  group_name from ").append(msngaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append(" and (CAMPAIGN_NAME = '(not set)')  group by CAMPAIGN_NAME,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                    gaMsnYOYQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnOrders) as gamsnOrders,")
                            .append(" sum(gamsnPRevenue) as gamsnPRevenue,sum(gamsnTax) as gamsnTax, sum(gamsnShipping) as gamsnShipping,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax, sum(GA_SHIPPING) as gamsnShipping,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING")
                            .append(" , account_id, replace(campaign_name,' ','_') as campaign_name  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  and adgroup_name is not null  group by  campaign_name ,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and upper(grpnames.campaign_name) = upper(b1.campaign_name) ) group by  week,year,Date_Range,group_name ")
                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" 'SEM Weekly' as  group_name from ").append(msngaStatsTableName).append(" where ")
                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year - 1).append(" and (CAMPAIGN_NAME = '(not set)')  group by CAMPAIGN_NAME,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                } else if (reportType != AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 1) {

                    gaMsnQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) k group by  week,year,Date_Range,group_name union all ");

                    gaMsnYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" ( select * from bing_campaign_structure where  ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) )) h group by  week,year,Date_Range,group_name union all ");

                } else if (reportType != AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 2) {
                    gaMsnQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) m group by  week,year,Date_Range,group_name union all ");

                    gaMsnYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue, sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) ))  j group by  week,year,Date_Range,group_name union all ");
                }
            }
//            Yahoo Gemini Analytics Queries Starts here 
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {

                if (reportType == AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 1) {
                    gaYahGemQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahOrders) as gayahOrders,")
                            .append(" sum(gayahRevenue) as gayahRevenue,group_name,0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id,campaign_name from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append(" group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and  upper(grpnames.campaign_name) = upper(b1.campaign_name) )  group by  week,year,Date_Range,group_name  ")
                            //                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            //                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            //                            .append(" 'SEM Weekly' as  group_name from ").append(yahgaStatsTableName).append(" where ")
                            //                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            //                            .append(year).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                    gaYahGemYOYQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahOrders) as gayahOrders,")
                            .append(" sum(gayahRevenue) as gayahRevenue,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id,campaign_name from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("   group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and upper(grpnames.campaign_name) = upper(b1.campaign_name) )  group by  week,year,Date_Range,group_name ")
                            //                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            //                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            //                            .append(" 'SEM Weekly' as  group_name from ").append(yahgaStatsTableName).append(" where ")
                            //                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            //                            .append(year - 1).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                } else if (reportType == AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 2) {
                    gaYahGemQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahOrders) as gayahOrders,")
                            .append(" sum(gayahPRevenue) as gayahPRevenue,sum(gayahTax) as gayahTax, sum(gayahShipping) as gayahShipping,group_name,0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax, sum(GA_SHIPPING) as gayahShipping,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING")
                            .append(" , account_id,campaign_name  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  and adgroup_name is not null  group by  campaign_name ,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and upper(grpnames.campaign_name) = upper(b1.campaign_name) ) group by  week,year,Date_Range,group_name union all ")
                            //                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            //                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            //                            .append(" 'SEM Weekly' as  group_name from ").append(yahgaStatsTableName).append(" where ")
                            //                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            //                            .append(year).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id ,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                    gaYahGemYOYQuery.append("select ").append(i).append(" as week_no, Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahOrders) as gayahOrders,")
                            .append(" sum(gayahPRevenue) as gayahPRevenue,sum(gayahTax) as gayahTax, sum(gayahShipping) as gayahShipping,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax, sum(GA_SHIPPING) as gayahShipping,group_name ")
                            .append(" from (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING")
                            .append(" , account_id,campaign_name  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(clientId).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  and adgroup_name is not null  group by  campaign_name ,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.account_id and upper(grpnames.campaign_name) = upper(b1.campaign_name) ) group by  week,year,Date_Range,group_name union all ")
                            //                            .append(" union all select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            //                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            //                            .append(" 'SEM Weekly' as  group_name from ").append(yahgaStatsTableName).append(" where ")
                            //                            .append(isMonthTillDate).append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            //                            .append(year - 1).append(" and (campaign_name is null or adgroup_name is  null)  group by se_campaign_id ,year,account_id ")
                            .append(" )k group by group_name,week,year,week_no,Date_Range union all ");

                } else if (reportType != AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 1) {

                    gaYahGemQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) k group by  week,year,Date_Range,group_name union all ");

                    gaYahGemYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" ( select * from yahgemini_campaign_structure where  ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) )) h group by  week,year,Date_Range,group_name union all ");

                } else if (reportType != AutoReportConstants.STANDARD_REPORT && templateInfo.getRevenueType() == 2) {
                    gaYahGemQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) m group by  week,year,Date_Range,group_name union all ");

                    gaYahGemYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue, sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.reporting_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) ))  j group by  week,year,Date_Range,group_name union all ");
                }
            }
        }
    }

    private ExcelGenerator setDataToExcelGenerator() {
        excelGenerator = new ExcelGenerator(accountDetails);
        excelGenerator.setAppPath(appPath);
        excelGenerator.setReportsPath(reportsPath);
        excelGenerator.setDataSource(dataSource);
        excelGenerator.setTemplateInfo(templateInfo);
        excelGenerator.setReportingDate(reportingDate);
        excelGenerator.setPreparedOn(preparedOn);
        excelGenerator.setAutoReportsStatsCol(autoReportsStatsCol);
        excelGenerator.setFileName(fileName);
        excelGenerator.setBaseFileName(baseFileName);
        excelGenerator.setTarget(target);
        excelGenerator.setSql(finalStatsQuery);
        excelGenerator.setFinalTotals(finalTotals);  // look back period data w.r.t weeks
        excelGenerator.setMetric1(metric1);
        excelGenerator.setMetric2(metric2);
        excelGenerator.setMetric3(metric3);
        excelGenerator.setMetric4(metric4);
        excelGenerator.setAutoReportDownloadDao(autoReportDownloadDao);
        excelGenerator.setAutomationTemplateDao(automationTemplateDao);
        excelGenerator.setRevOrderSrc(revOrderSrc);
        excelGenerator.setGleRevenue(grevenue);
        excelGenerator.setMsnRevenue(mrevenue);
        excelGenerator.setYahGemRevenue(yrevenue);
        excelGenerator.setIs_mom(is_mom);
        excelGenerator.setFbCampSum(fbCampSum);
        return excelGenerator;
    }

    public String getWeeklyGroupReport(String grpIds) {
        String finalGrpIds = "";
        tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
        try {
            if (grpIds.equalsIgnoreCase("-1")) {
                finalGrpIds = "501,502";
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.BRAND_AND_NONBRAND_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
                isCustom = false;
            } else {
                finalGrpIds = grpIds;
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.GROUP_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
                isCustom = true;
            }

            getWeeklyQueries(tmpTablename, finalGrpIds, AutoReportConstants.GROUP_REPORT, false);
//            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);
            if (grpIds.equalsIgnoreCase("-1")) {
                getBrandNonBrandMonthlyOrWeeklyMergeQueries(tmpTablename);
            } else {
//                getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);
                getCustomGroupMonthlyOrWeeklyMergeQueries(tmpTablename);
            }

            String baiscTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by group_name, week_no desc";
            finalStatsQuery = "select date_range," + sqlColumnNames + ",group_name from " + tmpTablename + " where yearoveryear = 0 order by group_name,week_no desc";
            LOGGER.info("Sem Group query>>>>>>>>>>" + finalStatsQuery);
            finalTotals = new ArrayList();
            finalTotals = autoReportDownloadDao.loadObjectsForTotals(baiscTotalsQuery, finalTotals, colNamesWithTotal);

            excelGenerator = setDataToExcelGenerator();
            if (yearOverYear == true) {
                excelGenerator.setPrevYear(false);
            }
            excelGenerator.setReportColNames("Date Range," + reportColNames + ",group_name");
            excelGenerator.setIsGroupReport(true);
            if (grpIds.equalsIgnoreCase("-1")) {
                excelGenerator.setLoopCnt(weeklyLoopCnt - 2);
                excelGenerator.setReportName(AutoReportConstants.SEM_WEEKLY_BRAND_AND_NON_BRAND_REPORT);
                excelGenerator.setReportType(AutoReportConstants.BRAND_AND_NONBRAND_REPORT);
                excelGenerator.setGroup_name("Brand");
                excelGenerator.setGroup_name1("Non Brand");
            } else {
                excelGenerator.setReportName(AutoReportConstants.SEM_WEEKLY_CATEGORY_REPORT);
                excelGenerator.setReportType(AutoReportConstants.GROUP_REPORT);
            }
            excelGenerator.generateNewXLSXReport();

            if (yearOverYear == true) {
                excelGenerator.setGrpNamesSql("SELECT GROUP_NAME FROM lxr_kpi_group_master WHERE GROUP_ID IN(" + finalGrpIds + ") ORDER BY GROUP_ID");
                excelGenerator.setYearOverYear(yearOverYear);
                finalStatsQuery = "select date_range,week_no," + sqlColumnNames + ",group_name,yearoveryear from " + tmpTablename + "  where group_name=";//new code
                excelGenerator.setSql(finalStatsQuery);
                excelGenerator.generateNewXLSXReport();
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            autoReportDownloadDao.dropTable(tmpTablename);
            LOGGER.info("Temporary Successfully Deleted....");
        }
        return null;
    }

    public String getCmpPerformanceReport(int isWeekly) {

        try {

            if (isWeekly == AutoReportConstants.WEEKLY) {
                tmpTablename = "tmp_AR_WEEKLY" + templateId + "_" + cal.getTimeInMillis();
                getWeeklyQueries(tmpTablename, "", CommonConstants.CAMPAIGN);
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.CAMPAIGN_PERFORMANCE_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            } else {
                tmpTablename = "tmp_AR_MONTHLY" + templateId + "_" + cal.getTimeInMillis();
                getMonthlyQueries(tmpTablename, "", CommonConstants.CAMPAIGN);
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.CAMPAIGN_PERFORMANCE_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
            }

            getCmpPerfMonthlyOrWeeklyMergeQueries(tmpTablename);
            finalStatsQuery = "select DATE_RANGE,campaign_name," + sqlColumnNames + ",account_id from " + tmpTablename + " where campaign_name is not null order by account_id, campaign_name ";
            LOGGER.info("Campaing performance  query>>>>>>>>>>" + finalStatsQuery);
//
            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setPrevYear(true);
            excelGenerator.setReportColNames("Search Engine,Campaign Name," + reportColNames);
            excelGenerator.setReportName(AutoReportConstants.CAMPAIGN_PERFORMANCE_REPORT_NAME);
            excelGenerator.setReportType(AutoReportConstants.CAMPAIGN_PERFORMANCE_REPORT_WEEKLY);
            return excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            autoReportDownloadDao.dropTable(tmpTablename);
            LOGGER.info("Temporary Successfully Deleted....");
        }
        return "";
    }

    public String getAdgrpPerformanceReport(int isWeekly) {

        try {

            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            if (isWeekly == AutoReportConstants.WEEKLY) {
                getWeeklyQueries(tmpTablename, "", CommonConstants.ADGROUP);
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.ADGROUP_PERFORMANCE_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            } else {
                getMonthlyQueries(tmpTablename, "", CommonConstants.ADGROUP);
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.ADGROUP_PERFORMANCE_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
            }

            getAdgrpPerfMonthlyOrWeeklyMergeQueries(tmpTablename);
            finalStatsQuery = "select DATE_RANGE,campaign_name,adgroup_name," + sqlColumnNames + ",account_id from " + tmpTablename + " where campaign_name is not null and adgroup_name is not null order by account_id, campaign_name, adgroup_name";
            LOGGER.info("Adgroup performance  query>>>>>>>>>>" + finalStatsQuery);
//
            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setPrevYear(true);
            excelGenerator.setReportColNames("Search Engine,Campaign Name,Adgroup Name," + reportColNames);
            excelGenerator.setReportName(AutoReportConstants.ADGROUP_PERFORMANCE_REPORT_NAME);
            excelGenerator.setReportType(AutoReportConstants.ADGROUP_PERFORMANCE_REPORT_WEEKLY);
            return excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            autoReportDownloadDao.dropTable(tmpTablename);
            LOGGER.info("Temporary Successfully Deleted....");
        }
        return "";
    }

    public String getKeywordPerformanceReport(int isWeekly, int convKeyReport) {

        try {
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            if (isWeekly == AutoReportConstants.WEEKLY) {
                getWeeklyQueries(tmpTablename, "", AutoReportConstants.CONVERTING_KEYWORD);
            } else {
                getMonthlyQueries(tmpTablename, "", AutoReportConstants.CONVERTING_KEYWORD);
            }
            if (convKeyReport == AutoReportConstants.CONVERTING_KEYWORD) {
                if (isWeekly == AutoReportConstants.WEEKLY) {
                    fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.CONVERTING_KEYWORD_PERFORMANCE_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
                } else if (isWeekly == AutoReportConstants.MONTHLY) {
                    fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.CONVERTING_KEYWORD_PERFORMANCE_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
                }
            } else if (convKeyReport == AutoReportConstants.TOP_COST_NONCONV_KEYWORD) {
                if (isWeekly == AutoReportConstants.WEEKLY) {
                    fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.TOP_COST_NONCONV_KEYWORD_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
                } else if (isWeekly == AutoReportConstants.MONTHLY) {
                    fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.TOP_COST_NONCONV_KEYWORD_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
                }
            }
            getKeywordPerfMonthlyOrWeeklyMergeQueries(tmpTablename);

            if (gorders.equalsIgnoreCase("")) {
                gorders = "gorders";
            }
            if (morders.equalsIgnoreCase("")) {
                morders = "morders";
            }
            if (yorders.equalsIgnoreCase("")) {
                yorders = "yorders";
            }
            if (convKeyReport == AutoReportConstants.CONVERTING_KEYWORD) {

                finalStatsQuery = "select DATE_RANGE,campaign_name,keyword_name,quality_score," + sqlColumnNames + ",account_id from " + tmpTablename + " where campaign_name is not null and (" + gorders + " > 0 or " + morders + " > 0  or " + yorders + " > 0) and (Gcost>0 or Mcost>0 or Ycost>0) order by account_id, campaign_name, keyword_name ";
                LOGGER.info("Conv Keyword performance  query: " + finalStatsQuery);
            } else {  // Non Converting Keyword Performance Report
//           
                finalStatsQuery = "select DATE_RANGE,campaign_name,keyword_name,quality_score," + sqlColumnNames + ",account_id from " + tmpTablename + " where campaign_name is not null and (" + gorders + " = 0 or " + morders + " = 0  or " + yorders + " = 0) and (Gcost>0 or Mcost>0 or Ycost>0) order by account_id, campaign_name, keyword_name ";
                LOGGER.info("Cost Taking Keyword performance  query: " + finalStatsQuery);

            }

            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setPrevYear(true);
            excelGenerator.setReportColNames("Search Engine,Campaign Name,Keyword Name,Quality Score," + reportColNames);
            excelGenerator.setReportName(convKeyReport == AutoReportConstants.CONVERTING_KEYWORD ? AutoReportConstants.CONVERTING_KEYWORD_PERFORMANCE_REPORT_NAME : AutoReportConstants.TOP_COST_NONCONV_KEYWORD_REPORT_NAME);
            if (convKeyReport == AutoReportConstants.CONVERTING_KEYWORD) {
                if (isWeekly == AutoReportConstants.WEEKLY) {
                    excelGenerator.setReportType(AutoReportConstants.CONVERTING_KEYWORD_PERFORMANCE_REPORT_WEEKLY);
                } else {
                    excelGenerator.setReportType(AutoReportConstants.CONVERTING_KEYWORD_PERFORMANCE_REPORT_MONTHLY);
                }
            } else if (convKeyReport == AutoReportConstants.TOP_COST_NONCONV_KEYWORD) {
                if (isWeekly == AutoReportConstants.WEEKLY) {
                    excelGenerator.setReportType(AutoReportConstants.TOP_COST_NONCONV_KEYWORD_REPORT_WEEKLY);
                } else {
                    excelGenerator.setReportType(AutoReportConstants.TOP_COST_NONCONV_KEYWORD_REPORT_MONTHLY);
                }
            }
            return excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            autoReportDownloadDao.dropTable(tmpTablename);
            LOGGER.info("Temporary Successfully Deleted....");
        }
        return "";
    }

    public String getMTDPerformanceReport() {
        try {
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            if (templateInfo.getReportingType() == 1) {
                getTillDayWeeklyQueries(tmpTablename, "505", AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY);
            } else {
                getTillDateWeeklyQueries(tmpTablename, "505", AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY);
            }

            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);

            finalStatsQuery = "select week_no,date_range," + sqlColumnNames + ",yearoveryear from " + tmpTablename + "  order by week_no desc";
            LOGGER.info("Sem MTD Weekly query>>>>>>>>>>" + finalStatsQuery);

            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setReportColNames("Week_no,Date Range," + reportColNames);
            excelGenerator.setReportName(AutoReportConstants.MTD_REPORT_NAME);
            excelGenerator.setReportType(AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY);
            return excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
        return "";
    }

    public String getYTDPerformanceReport() {
        try {
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            if (templateInfo.getReportingType() == 1) {
                getTillDayWeeklyQueries(tmpTablename, "506", AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY);
            } else {
                getTillDateWeeklyQueries(tmpTablename, "506", AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY);
            }

            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);

            finalStatsQuery = "select week_no,date_range," + sqlColumnNames + ",yearoveryear from " + tmpTablename + "  order by week_no desc";
            LOGGER.info("Sem YTD Weekly query>>>>>>>>>>" + finalStatsQuery);

            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setReportColNames("Week_no,Date Range," + reportColNames);
            excelGenerator.setReportName(AutoReportConstants.YTD_REPORT_NAME);
            excelGenerator.setReportType(AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY);
            return excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
        return "";
    }

    public void getTillDayWeeklyQueries(String tableName, String grpIds, int reportType) {
        gleseStatsTableName = "AW_ReportAdGroup";
        glegaStatsTableName = "lxr_reportsgastats";

        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }
        msnseStatsTableName = "Bing_Report_AdgroupStats";
        msngaStatsTableName = "lxr_reportsgastats";

        yahseStatsTableName = "Yahoo_Report_AdgroupStats";
        yahgaStatsTableName = "lxr_reportsgastats";

        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }
        getTmpGroupTableSql(tableName);

        gleQuery = new StringBuilder();
        gaGleQuery = new StringBuilder();

        gleYOYQuery = new StringBuilder();
        gaGleYOYQuery = new StringBuilder();

        gleMergeQuery = new StringBuilder();
        gaGleMergeQuery = new StringBuilder();

        gleYOYMergeQuery = new StringBuilder();
        gaGleYOYMergeQuery = new StringBuilder();

        msnQuery = new StringBuilder();
        gaMsnQuery = new StringBuilder();

        msnYOYQuery = new StringBuilder();
        gaMsnYOYQuery = new StringBuilder();

        msnMergeQuery = new StringBuilder();
        gaMsnMergeQuery = new StringBuilder();

        msnYOYMergeQuery = new StringBuilder();
        gaMsnYOYMergeQuery = new StringBuilder();

        int wkStrt = 0;
        if (weekStart == 7) {
            wkStrt = 1;
        } else {
            wkStrt = weekStart + 1;
        }
        cal = Calendar.getInstance();

        if (isCustomDownload()) { // for custom download
            cal.set(Calendar.DAY_OF_WEEK, wkStrt);

            if (cal.getTime().after(Calendar.getInstance().getTime())) {
                cal.add(Calendar.WEEK_OF_MONTH, -1);
            }
        } else {
            cal.add(Calendar.DATE, -2);
            cal.set(Calendar.DAY_OF_WEEK, wkStrt);
        }

        fcalYOY = Calendar.getInstance();
        fcalYOY.set(Calendar.YEAR, (cal.get(Calendar.YEAR) - 1));
        fcalYOY.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR));
        fcalYOY.set(Calendar.DAY_OF_WEEK, wkStrt);

        int loopCnt = 0;
        if (reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
            loopCnt = 1;
        } else {
            loopCnt = cal.get(Calendar.MONTH) + 1;
        }

        for (int i = 0; i < loopCnt; i++) {
            cal.add(Calendar.DATE, -1);
            tdate = cal.get(Calendar.DAY_OF_MONTH);
            tyear = cal.get(Calendar.YEAR);
            tmonthNo = cal.get(Calendar.MONTH);
            tmonthName = CommonFunctions.getMonthName(tmonthNo);
            toDate = new DateConverter(cal).getSQLDateForm1();
            fcalYOY.add(Calendar.DATE, -1);
            yoytdate = fcalYOY.get(Calendar.DAY_OF_MONTH);
            yoytyear = fcalYOY.get(Calendar.YEAR);
            yoytmonthNo = fcalYOY.get(Calendar.MONTH);
            yoytmonthName = CommonFunctions.getMonthName(yoytmonthNo);
            yoyToDate = new DateConverter(fcalYOY).getSQLDateForm1();

            if (reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                cal.set(Calendar.MONTH, Calendar.JANUARY);
                cal.set(Calendar.WEEK_OF_YEAR, 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);

                fcalYOY.set(Calendar.MONTH, Calendar.JANUARY);
                fcalYOY.set(Calendar.WEEK_OF_YEAR, 1);
                fcalYOY.set(Calendar.DATE, 1);
            } else {

                if (cal.get(Calendar.WEEK_OF_MONTH) == 1) {
                    cal.add(Calendar.DATE, -(tdate));
                    cal.add(Calendar.DATE, -(cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
                } else {
                    cal.add(Calendar.DATE, -(tdate - 1));
                }

                if (fcalYOY.get(Calendar.WEEK_OF_MONTH) == 1) {
                    fcalYOY.add(Calendar.DATE, -(yoytdate));
                    fcalYOY.add(Calendar.DATE, -(fcalYOY.getActualMaximum(Calendar.DAY_OF_MONTH) - 1));
                } else {
                    fcalYOY.add(Calendar.DATE, -(yoytdate - 1));
                }

            }

            cal.set(Calendar.DAY_OF_WEEK, wkStrt);
            fdate = cal.get(Calendar.DATE);
            fyear = cal.get(Calendar.YEAR);
            fmonthNo = cal.get(Calendar.MONTH);
            fmonthName = CommonFunctions.getMonthName(fmonthNo);
            fromDate = new DateConverter(cal).getSQLDateForm1();

            fcalYOY.set(Calendar.DAY_OF_WEEK, wkStrt);
            yoyfdate = fcalYOY.get(Calendar.DATE);
            yoyfyear = fcalYOY.get(Calendar.YEAR);
            yoyfmonthNo = fcalYOY.get(Calendar.MONTH);
            yoyfmonthName = CommonFunctions.getMonthName(yoyfmonthNo);
            yoyFromDate = new DateConverter(fcalYOY).getSQLDateForm1();

            if (i == 0) {
                dateRange = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
            } else {
                dateRange = fmonthName + " " + fdate + "-" + tmonthName + " " + tdate;
            }
            yoyDateRange = yoyfmonthName + " " + yoyfdate + "-" + yoytmonthName + " " + yoytdate;

            durNo = fmonthNo;
            year = fyear;

            if (i == 0) {
                reportingDate = dateRange;
                dateRange = fmonthName + " " + fdate + "-" + tmonthName + " " + tdate;
            }
            if (reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                getGroupMonthlyOrWeeklyTillDateQueries((i + 1), "RM" + (i + 1), grpIds, reportType);
            }
        }
    }

    public void getGroupMonthlyOrWeeklyTillDateQueries(int i, String week, String grpIds, int reportType) {
        seAccId = accountDetailsDao.getAccountSeId(gAcc_id);
        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }
        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }
        String[] grpIdStr = grpIds.split(",");
        StringBuilder wtd1 = new StringBuilder();
        for (int j = 0; j < grpIdStr.length; j++) {
            wtd1.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id, ").append(gAcc_id).append(" as account_id, ").append("adgroup_id,2 as component_level, ").append(grpIdStr[j]).append(" as group_id ").append("  from adgroup_structure where campaign_id in (")
                    .append(" select campaign_id from reports_automation_groups where component_level=1 and ")
                    .append("account_id = ").append(gAcc_id).append(" and group_id = ").append(grpIdStr[j]).append(")");
        }
        StringBuilder wtd2 = new StringBuilder();
        for (int j = 0; j < grpIdStr.length; j++) {
            wtd2.append(" union ")
                    .append(" (select ").append(clientId).append(" as client_id, ").append(mAcc_id).append(" as account_id, ").append("adgroup_id,2 as component_level, ").append(grpIdStr[j]).append(" as group_id ").append("  from bing_adgroup_structure where campaign_id in (")
                    .append(" select campaign_id from reports_automation_groups where component_level=1 and ")
                    .append("account_id = ").append(mAcc_id).append(" and group_id = ").append(grpIdStr[j]).append("))");
        }
        StringBuilder wtd3 = new StringBuilder();
        for (int j = 0; j < grpIdStr.length; j++) {
            wtd3.append(" union ")
                    .append(" (select ").append(clientId).append(" as client_id, ").append(yahAcc_id).append(" as account_id, ").append("adgroup_id,2 as component_level, ").append(grpIdStr[j]).append(" as group_id ").append("  from yahgemini_adgroup_structure where campaign_id in (")
                    .append(" select campaign_id from reports_automation_groups where component_level=1 and ")
                    .append("account_id = ").append(yahAcc_id).append(" and group_id = ").append(grpIdStr[j]).append("))");
        }
        gleQuery.append("select ").append(i).append(" as week_no,").append(" Week,").append(tyear).append(" as year, Date_Range,sum(impressions) as gimpressions,sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(conversionvalue) as ")
                .append(" gRevenue,case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS gavg_pos,group_name,0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",conversionvalue,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,Date_Range,b1.adgroup_id from (select '").append(week).append("'  as Week,'")
                .append(dateRange).append("' as Date_Range, impressions,clicks ,cost ,").append(orderType).append(" , conversionvalue, (AVERAGE_POSITION*Impressions) as avg_pos,")
                .append(" adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                .append("account_id = ").append(seAccId).append(" and ").append("day between '").append(fromDate).append("' and '").append(toDate).append("'")
                .append(" ) b1 ").append(" join").append(" (   ").append(" select * from ")
                .append(" (select client_id,account_id,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(wtd1.toString()).append("  ) grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id)")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONV_ONE_PER_CLICK , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,(select group_name from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append(")) as group_name,'").append(week).append("'  as Week,  '")
                .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from dual)")
                .append(")f group by WEEK,  DATE_RANGE, GROUP_NAME  union all ");

        msnQuery.append("select ").append(i).append(" as week_no,").append(" Week,").append(tyear).append(" as year, Date_Range,sum(impressions) as mimpressions,sum(clicks) as mclicks,sum(spend) as mcost, sum(").append(orderType).append(") as mOrders,sum(revenue) as ")
                .append(" mRevenue,avg(avg_pos) AS mavg_pos,group_name,0 as yearoveryear from ( select impressions, clicks, spend, ").append(orderType).append(", revenue, avg_pos, adgroup_name")
                .append(", group_name, Week, Date_Range, b1.adgroup_id from (select '").append(week).append("'  as Week,'")
                .append(dateRange).append("' as Date_Range, impressions,clicks , spend, ").append(orderType).append(", revenue, avg_pos,")
                .append(" adgroup_name, adgroup_id from ").append(msnseStatsTableName).append(" where ")
                .append("se_account_id = ").append(msnseAccId).append(" and ").append("day between '").append(fromDate).append("' and '").append(toDate).append("'")
                .append(" ) b1 ").append(" join").append(" ( ").append("select * from ")
                .append(" ((select client_id,account_id,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" )) ").append(wtd2.toString()).append("  ) grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id)")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONV_ONE_PER_CLICK , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,(select group_name from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append(")) as group_name,'").append(week).append("'  as Week,  '")
                .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from dual)")
                .append(")f group by WEEK,  DATE_RANGE, GROUP_NAME  union all ");

        yahGemQuery.append("select ").append(i).append(" as week_no,").append(" Week,").append(tyear).append(" as year, Date_Range,sum(impressions) as yimpressions,sum(clicks) as yclicks,sum(spend) as ycost, sum(").append(orderType).append(") as yOrders,sum(revenue) as ")
                .append(" yRevenue,avg(avg_pos) AS yavg_pos,group_name,0 as yearoveryear from ( select impressions, clicks, spend, ").append(orderType).append(", revenue, avg_pos, adgroup_name")
                .append(", group_name, Week, Date_Range, b1.adgroup_id from (select '").append(week).append("'  as Week,'")
                .append(dateRange).append("' as Date_Range, impressions,clicks , spend, ").append(orderType).append(", 0 as revenue, avg_pos,")
                .append(" adgroup_name, adgroup_id from ").append(yahseStatsTableName).append(" where ")
                .append("se_account_id = ").append(yahseAccId).append(" and ").append("day between '").append(fromDate).append("' and '").append(toDate).append("'")
                .append(" ) b1 ").append(" join").append(" ( ").append("select * from ")
                .append(" ((select client_id,account_id,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" )) ").append(wtd3.toString()).append("  ) grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id)")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONV_ONE_PER_CLICK , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,(select group_name from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append(")) as group_name,'").append(week).append("'  as Week,  '")
                .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from dual)")
                .append(")f group by WEEK,  DATE_RANGE, GROUP_NAME  union all ");

        gleYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,").append(yoytyear).append(" as year, Date_Range,sum(impressions) as gimpressions,sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(conversionvalue) as ")
                .append(" gRevenue,case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS gavg_pos,group_name,1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",conversionvalue,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,Date_Range,b1.adgroup_id from (select '").append(week).append("'  as Week,'")
                .append(yoyDateRange).append("' as Date_Range, impressions,clicks ,cost ,").append(orderType).append(" , conversionvalue, (AVERAGE_POSITION*Impressions) as avg_pos,")
                .append(" adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                .append("account_id = ").append(seAccId).append(" and ").append("day between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                .append(" ) b1 ").append(" join").append(" (   ").append(" select * from ")
                .append(" (select client_id,account_id,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(wtd1.toString()).append("  ) grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id)")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONV_ONE_PER_CLICK , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,(select group_name from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append(")) as group_name,'").append(week).append("'  as Week,  '")
                .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from dual)")
                .append(")f group by WEEK,  DATE_RANGE, GROUP_NAME  union all ");

        msnYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,").append(yoytyear).append(" as year, Date_Range,sum(impressions) as mimpressions,sum(clicks) as mclicks,sum(spend) as mcost, sum(").append(orderType).append(") as mOrders,sum(revenue) as ")
                .append(" mRevenue,avg(avg_pos) AS mavg_pos,group_name,1 as yearoveryear from ( select impressions, clicks, spend, ").append(orderType).append(", revenue, avg_pos, adgroup_name")
                .append(", group_name, Week, Date_Range, b1.adgroup_id from (select '").append(week).append("'  as Week,'")
                .append(yoyDateRange).append("' as Date_Range, impressions,clicks , spend, ").append(orderType).append(", revenue, avg_pos,")
                .append(" adgroup_name, adgroup_id from ").append(msnseStatsTableName).append(" where ")
                .append("se_account_id = ").append(msnseAccId).append(" and ").append("day between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                .append(" ) b1 ").append(" join").append(" ( ").append("select * from ")
                .append(" ((select client_id,account_id,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" )) ").append(wtd2.toString()).append("  ) grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id)")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONV_ONE_PER_CLICK , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,(select group_name from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append(")) as group_name,'").append(week).append("'  as Week,  '")
                .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from dual)")
                .append(")f group by WEEK,  DATE_RANGE, GROUP_NAME  union all ");

        yahGemYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,").append(yoytyear).append(" as year, Date_Range,sum(impressions) as yimpressions,sum(clicks) as yclicks,sum(spend) as ycost, sum(").append(orderType).append(") as yOrders,sum(revenue) as ")
                .append(" yRevenue,avg(avg_pos) AS yavg_pos,group_name,1 as yearoveryear from ( select impressions, clicks, spend, ").append(orderType).append(", revenue, avg_pos, adgroup_name")
                .append(", group_name, Week, Date_Range, b1.adgroup_id from (select '").append(week).append("'  as Week,'")
                .append(yoyDateRange).append("' as Date_Range, impressions,clicks , spend, ").append(orderType).append(", 0 as revenue, avg_pos,")
                .append(" adgroup_name, adgroup_id from ").append(yahseStatsTableName).append(" where ")
                .append("se_account_id = ").append(yahseAccId).append(" and ").append("day between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                .append(" ) b1 ").append(" join").append(" ( ").append("select * from ")
                .append(" ((select client_id,account_id,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" )) ").append(wtd3.toString()).append("  ) grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id)")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONV_ONE_PER_CLICK , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,(select group_name from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append(")) as group_name,'").append(week).append("'  as Week,  '")
                .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from dual)")
                .append(")f group by WEEK,  DATE_RANGE, GROUP_NAME  union all ");

        if (templateInfo.getGoogleAnalytics() != 0) {
            String grpNm = "";
            if (reportType == AutoReportConstants.STANDARD_REPORT) {
                grpNm = "SEM Weekly";
            } else if (reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY) {
                grpNm = "MTD";
            } else if (reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                grpNm = "YTD";
            }
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && gAcc_id != 0) {

                if (reportType == AutoReportConstants.STANDARD_REPORT || reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {

                    gaGleQuery.append("select ").append(i).append(" as week_no, Week,").append(tyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gagleRevenue) as gagleRevenue,group_name, 0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                            .append(gAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append(" MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append(" MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1  and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                    gaGleYOYQuery.append("select ").append(i).append(" as week_no, Week,").append(yoytyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gagleRevenue) as gagleRevenue,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(gAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                }
            } else if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && gAcc_id != 0) {
                if (reportType == AutoReportConstants.STANDARD_REPORT || reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                    gaGleQuery.append("select ").append(i).append(" as week_no, Week,").append(tyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gaglePRevenue) as gaglePRevenue,sum(gagleTax) as gagleTax, sum(gagleShipping) as gagleShipping,group_name, 0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax, sum(GA_SHIPPING) as gagleShipping,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(gAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                    gaGleYOYQuery.append("select ").append(i).append(" as week_no, Week,").append(yoytyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gagleOrders) as gagleOrders,")
                            .append(" sum(gaglePRevenue) as gaglePRevenue,sum(gagleTax) as gagleTax, sum(gagleShipping) as gagleShipping,group_name,1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax, sum(GA_SHIPPING) as gagleShipping,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(gAcc_id).append(" as  accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1  and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id    =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 1 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                }
            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && mAcc_id != 0) {
                if (reportType == AutoReportConstants.STANDARD_REPORT || reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {

                    gaMsnQuery.append("select ").append(i).append(" as week_no, Week,").append(tyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnorders) as gamsnorders,")
                            .append(" sum(gamsnrevenue) as gamsnrevenue,group_name, 0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnorders,sum(GA_REVENUE) as gamsnrevenue,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(mAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2  and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2  and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                    gaMsnYOYQuery.append("select ").append(i).append(" as week_no, Week,").append(yoytyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnorders) as gamsnorders,")
                            .append(" sum(gamsnrevenue) as gamsnrevenue,group_name, 1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnorders,sum(GA_REVENUE) as gamsnrevenue,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(mAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");
                }
            } else if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && mAcc_id != 0) {
                if (reportType == AutoReportConstants.STANDARD_REPORT || reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {

                    gaMsnQuery.append("select ").append(i).append(" as week_no, Week,").append(tyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnorders) as gamsnorders,")
                            .append(" sum(gamsnPRevenue) as gamsnPRevenue,sum(gamsntax) as gamsntax, sum(gamsnshipping) as gamsnshipping,group_name, 0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnorders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsntax, sum(GA_SHIPPING) as gamsnshipping,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(mAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                    gaMsnYOYQuery.append("select ").append(i).append(" as week_no, Week,").append(yoytyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gamsnorders) as gamsnorders,")
                            .append(" sum(gamsnPRevenue) as gamsnPRevenue,sum(gamsntax) as gamsntax, sum(gamsnshipping) as gamsnshipping,group_name, 1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnorders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsntax, sum(GA_SHIPPING) as gamsnshipping,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(mAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 2 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");
                }
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 1 && yahAcc_id != 0) {
                if (reportType == AutoReportConstants.STANDARD_REPORT || reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {

                    gaYahGemQuery.append("select ").append(i).append(" as week_no, Week,").append(tyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahorders) as gayahorders,")
                            .append(" sum(gayahrevenue) as gayahrevenue,group_name, 0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders,sum(GA_REVENUE) as gayahrevenue,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(yahAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3  and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                    gaYahGemYOYQuery.append("select ").append(i).append(" as week_no, Week,").append(yoytyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahorders) as gayahorders,")
                            .append(" sum(gayahrevenue) as gayahrevenue,group_name, 1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders,sum(GA_REVENUE) as gayahrevenue,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(yahAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND  SEARCH_ENGINE_ID = 3   and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name, SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3  and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");
                }
            } else if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 2 && yahAcc_id != 0) {
                if (reportType == AutoReportConstants.STANDARD_REPORT || reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {

                    gaYahGemQuery.append("select ").append(i).append(" as week_no, Week,").append(tyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahorders) as gayahorders,")
                            .append(" sum(gayahPRevenue) as gayahPRevenue,sum(gayahtax) as gayahtax, sum(gayahshipping) as gayahshipping,group_name, 0 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahtax, sum(GA_SHIPPING) as gayahshipping,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(yahAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3 and  se_date between '").append(fromDate).append("' and '").append(toDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");

                    gaYahGemYOYQuery.append("select ").append(i).append(" as week_no, Week,").append(yoytyear).append(" as year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(gayahorders) as gayahorders,")
                            .append(" sum(gayahPRevenue) as gayahPRevenue,sum(gayahtax) as gayahtax, sum(gayahshipping) as gayahshipping,group_name, 1 as yearoveryear from (")
                            .append(" select ").append(i).append(" as week_no,").append(" Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahtax, sum(GA_SHIPPING) as gayahshipping,group_name ")
                            .append(" from ( select * from  (select ").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                            //                            .append(" account_id accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(yahAcc_id).append(" as accId,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(" client_id   =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            //                            .append("'  group by campaign_name,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append("'  group by campaign_name,SEARCH_ENGINE_ID ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.accId and upper(grpnames.campaign_name) = upper(b1.campaign) ))p group by  week,Date_Range,group_name union all ")
                            .append("select ").append(i).append(" as week_no,").append("'").append(week).append("' as Week,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING,")
                            .append(" '").append(grpNm).append("' as  group_name from ").append(glegaStatsTableName).append(" where ")
                            .append("     client_id = ").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("  MEDIUM_TYPE = 1 AND SEARCH_ENGINE_ID = 3 and  se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate)
                            .append("' and campaign_name='(not set)'  group by upper(campaign_name),account_id ")
                            .append(" )h group by group_name,week,week_no,Date_Range union all ");
                }
            }
        }

    }

    public void getWeeklyQueries(String tableName, String grpIds, int reportType) {
        seAccId = accountDetailsDao.getAccountSeId(gAcc_id);
        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }
        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }
        if (fbAcc_id != 0) {
            fbseAccId = accountDetailsDao.getAccountSeId(fbAcc_id);
        }
        gleseStatsTableName = "ne_autoreportsweeklysestats";
        glegaStatsTableName = "ne_autoreportsweeklygastats";
        durCondition = "week_no";

        msnseStatsTableName = "ne_autoreportsweeklybingsestats";
        msngaStatsTableName = "ne_autoreportsweeklybinggastat";

        yahseStatsTableName = "ne_autoreportsyahweeklysestats";
        yahgaStatsTableName = "ne_autoreportsyahweeklygastats";

        isMonthTillDate = "";

        gleQuery = new StringBuilder();
        gaGleQuery = new StringBuilder();
        gleYOYQuery = new StringBuilder();
        gaGleYOYQuery = new StringBuilder();
        gleMergeQuery = new StringBuilder();
        gaGleMergeQuery = new StringBuilder();
        gleYOYMergeQuery = new StringBuilder();
        gaGleYOYMergeQuery = new StringBuilder();

        msnQuery = new StringBuilder();
        gaMsnQuery = new StringBuilder();
        msnYOYQuery = new StringBuilder();
        gaMsnYOYQuery = new StringBuilder();
        msnMergeQuery = new StringBuilder();
        gaMsnMergeQuery = new StringBuilder();
        msnYOYMergeQuery = new StringBuilder();
        gaMsnYOYMergeQuery = new StringBuilder();

        yahGemQuery = new StringBuilder();
        gaYahGemQuery = new StringBuilder();
        yahGemYOYQuery = new StringBuilder();
        gaYahGemYOYQuery = new StringBuilder();
        yahGemMergeQuery = new StringBuilder();
        gaYahGemMergeQuery = new StringBuilder();
        yahGemYOYMergeQuery = new StringBuilder();
        gaYahGemYOYMergeQuery = new StringBuilder();

        getTmpGroupTableSql(tableName);

        cal = Calendar.getInstance();
        fcalYOY = Calendar.getInstance();
        if (isCustomDownload()) { // for custom download
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.DAY_OF_WEEK, weekStart);

            fcalYOY.add(Calendar.DATE, -1);
            fcalYOY.set(Calendar.DAY_OF_WEEK, weekStart);

            if (cal.getTime().after(Calendar.getInstance().getTime())) {
                cal.add(Calendar.WEEK_OF_MONTH, -1);
                fcalYOY.add(Calendar.WEEK_OF_MONTH, -1);

            }
        } else {
            cal.add(Calendar.DATE, -2);
            cal.set(Calendar.DAY_OF_WEEK, weekStart); // weekStart is fiscal week start day with numeric value.
            fcalYOY.add(Calendar.DATE, -2);
            fcalYOY.set(Calendar.DAY_OF_WEEK, weekStart);
        }
        sqlQuery = new StringBuilder();

        int loopCnt = 0; // weekl
        if (reportType == CommonConstants.CAMPAIGN || reportType == CommonConstants.ADGROUP || reportType == AutoReportConstants.CONVERTING_KEYWORD) {
            loopCnt = 2;
        } else {
            loopCnt = weeklyLoopCnt;
        }

        for (int i = 1; i < loopCnt; i++) {
            if (i != 1) {
                cal.add(Calendar.DATE, -1);
                fcalYOY.add(Calendar.DATE, -1);
            }

            cal.add(Calendar.DATE, -6);
            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
            fdate = cal.get(Calendar.DATE);
            fyear = cal.get(Calendar.YEAR);
            fmonthNo = cal.get(Calendar.MONTH);
            fmonthName = CommonFunctions.getMonthName(fmonthNo);
            // fcalYOY.set(Calendar.DAY_OF_WEEK, weekStart);
            fcalYOY.add(Calendar.DATE, -6);
            fcalYOY.set(Calendar.WEEK_OF_YEAR, weekOfYear);
            fcalYOY.set(Calendar.YEAR, fyear - 1);
            fcalYOY.set(Calendar.MONTH, fmonthNo);

            yoyfdate = fcalYOY.get(Calendar.DATE);
            yoyfyear = fcalYOY.get(Calendar.YEAR);
            yoyfmonthNo = fcalYOY.get(Calendar.MONTH);
            yoyfmonthName = CommonFunctions.getMonthName(yoyfmonthNo);

//            fromDate = new DateConverter(cal).getOracleDateForm();
//            fromDate = new DateConverter(cal).getSQLDateForm1();
            fromDate = (simpleDateFormat.format(cal.getTime()));
            yoyFromDate = new DateConverter(fcalYOY).getOracleDateForm();
            //String yoyFromDate = calYOY;

            cal.add(Calendar.DATE, 6);
            fcalYOY.add(Calendar.DATE, 6);

            tdate = cal.get(Calendar.DATE);
            tyear = cal.get(Calendar.YEAR);
            tmonthNo = cal.get(Calendar.MONTH);
            tmonthName = CommonFunctions.getMonthName(tmonthNo);

            yoytdate = fcalYOY.get(Calendar.DATE);
            yoytyear = fcalYOY.get(Calendar.YEAR);
            yoytmonthNo = fcalYOY.get(Calendar.MONTH);
            yoytmonthName = CommonFunctions.getMonthName(yoytmonthNo);

            toDate = (simpleDateFormat.format(cal.getTime()));
//            toDate = new DateConverter(cal).getSQLDateForm1();
//            toDate = new DateConverter(cal).getOracleDateForm();
            yoyToDate = new DateConverter(fcalYOY).getOracleDateForm();
            dateRange = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
            yoyDateRange = yoyfmonthName + " " + yoyfdate + "," + " " + yoyfyear + "-" + yoytmonthName + " " + yoytdate + "," + " " + yoytyear;
            cal.add(Calendar.DATE, -6);
            fcalYOY.add(Calendar.DATE, -6);
            if (i == 1) {
                reportingDate = dateRange;
            }
            durNo = weekOfYear;

            year = tyear;
            if (reportType == AutoReportConstants.GROUP_REPORT || reportType == AutoReportConstants.STANDARD_REPORT) {
                getGroupMonthlyOrWeeklyQueries(i, grpIds, reportType);
            } else if (reportType == CommonConstants.CAMPAIGN) {
                getCampPerMonthlyOrWeeklyQueries(i, grpIds);
            } else if (reportType == CommonConstants.ADGROUP) {
                getAdgrpPerMonthlyOrWeeklyQueries(i, grpIds);//Comment this when using new tables(changing table name and durconditions is enough)
            } else if (reportType == AutoReportConstants.CONVERTING_KEYWORD) {
                getConvPerformanceWeeklyQueries(i);
            }

        }
    }

    public void getCampPerMonthlyOrWeeklyQueries(int i, String grpIds) {
        LOGGER.info("Campaign Performance Report has started");
        gleQuery.append("( select  ").append(i).append(" as week_no,")
                .append("  Week, Date_Range,sum(impressions) as gimpressions,sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as  gRevenue,"
                + " case Sum(impressions)  when 0 then 0  else ( sum(avg_pos) / sum(impressions) ) end  AS gavg_pos,")
                .append("  campaign_name,account_id,campaign_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,Week,Date_Range,campaign_name,account_id,campaign_id from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos, client_id ,account_id, ")
                .append(" replace(c.campaign_name,' ','_') as campaign_name,c.campaign_id   from ").append(gleseStatsTableName).append(" s , (select distinct(campaign_id),campaign_name from AW_ReportCampaign ) c").append(" where  s.campaign_id = c.campaign_id  and").append(isMonthTillDate)
                .append("  level_of_detail = 2 ").append(" and ").append("account_id = ")
                .append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) k ) h group by campaign_name,account_id,campaign_id)d");

        msnQuery.append("( select  ").append(i).append(" as week_no,")
                .append("  Week, Date_Range,sum(impressions) as mimpressions,sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as  mRevenue,"
                + " case Sum(impressions)  when 0 then 0  else ( sum(avg_pos) / sum(impressions) ) end  AS mavg_pos,")
                .append("  campaign_name,account_id,campaign_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,Week,Date_Range,campaign_name,account_id,campaign_id from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos, client_id ,account_id, ")
                .append(" replace(c.campaign_name,' ','_') as campaign_name,c.campaign_id   from ").append(msnseStatsTableName).append(" s , (select distinct(campaign_id),campaign_name from Bing_Report_CampaignStats ) c").append(" where  s.campaign_id = c.campaign_id  and").append(isMonthTillDate)
                .append("  level_of_detail = 2 ").append(" and ").append("account_id = ")
                .append(mAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) k ) h group by campaign_name,account_id,campaign_id)d");

        yahGemQuery.append("( select  ").append(i).append(" as week_no,")
                .append("  Week, Date_Range,sum(impressions) as yimpressions,sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as  yRevenue,"
                + " case Sum(impressions)  when 0 then 0  else ( sum(avg_pos) / sum(impressions) ) end  AS yavg_pos,")
                .append("  campaign_name,account_id,campaign_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,Week,Date_Range,campaign_name,account_id,campaign_id from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos, client_id ,account_id, ")
                .append(" replace(c.campaign_name,' ','_') as campaign_name,c.campaign_id   from ").append(yahseStatsTableName).append(" s , (select distinct(campaign_id),campaign_name from yahgemini_campaign_structure ) c").append(" where  s.campaign_id = c.campaign_id  and").append(isMonthTillDate)
                .append("  level_of_detail = 2 ").append(" and ").append("account_id = ")
                .append(yahAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) k ) h group by campaign_name,account_id,campaign_id)d");

        if (templateInfo.getGoogleAnalytics() != 0) {
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 1 && gAcc_id != 0) {
                gaGleQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,campaign_name,account_id,se_campaign_id  ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,campaign_name,account_id,se_campaign_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id from ").append(glegaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append(" level_of_detail = 2").append(" and  account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id ) )s) h group by campaign_name,account_id,week,date_range) v");
            } else if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 2 && gAcc_id != 0) {
                gaGleQuery.append(" ( select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,campaign_name,account_id,se_campaign_id   ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,campaign_name,account_id,se_campaign_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(glegaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("  ").append(" account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id ) )s)h group by campaign_name,account_id,week,date_range) k ");

            }

            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && mAcc_id != 0) {
                gaMsnQuery.append(" (  select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, GA_TRANSACTIONS as gamsnOrders,GA_REVENUE as gamsnRevenue,replace(campaign_name,' ','_') as campaign_name,account_id,se_campaign_id  ")
                        .append(" from ").append(msngaStatsTableName).append(" where ").append(" level_of_detail = 2").append(" and  account_id in (").append(mAcc_id).append(") and  ").append(isMonthTillDate).append(" ")
                        .append(durCondition).append(" = ").append(durNo).append(" and year = ").append(year).append(") v");
            } else if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && mAcc_id != 0) {
                gaMsnQuery.append(" (  select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, GA_TRANSACTIONS as gamsnOrders,GA_PRODUCT_REVENUE as gamsnRevenue,GA_TAX as gamsnTax,GA_SHIPPING as gamsnShipping,replace(campaign_name,' ','_') as campaign_name,account_id,se_campaign_id  ")
                        .append(" from ").append(msngaStatsTableName).append(" where ").append(" level_of_detail = 2").append(" and  account_id in (").append(mAcc_id).append(") and  ").append(isMonthTillDate).append(" ")
                        .append(durCondition).append(" = ").append(durNo).append(" and year = ").append(year).append(") v");
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 1 && yahAcc_id != 0) {
                gaYahGemQuery.append(" (  select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, GA_TRANSACTIONS as gayahOrders,GA_REVENUE as gayahRevenue,replace(campaign_name,' ','_') as campaign_name,account_id,se_campaign_id  ")
                        .append(" from ").append(yahgaStatsTableName).append(" where ").append(" level_of_detail = 2").append(" and  account_id in (").append(yahAcc_id).append(") and  ").append(isMonthTillDate).append(" ")
                        .append(durCondition).append(" = ").append(durNo).append(" and year = ").append(year).append(") v");

            } else if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 2 && yahAcc_id != 0) {
                gaYahGemQuery.append(" (  select ").append(i).append(" as week_no,").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, GA_TRANSACTIONS as gayahOrders,GA_PRODUCT_REVENUE as gayahPRevenue,GA_TAX as gayahTax,GA_SHIPPING as gayahShipping,replace(campaign_name,' ','_') as campaign_name,account_id,se_campaign_id  ")
                        .append(" from ").append(yahgaStatsTableName).append(" where ").append(" level_of_detail = 2").append(" and  account_id in (").append(yahAcc_id).append(") and  ").append(isMonthTillDate).append(" ")
                        .append(durCondition).append(" = ").append(durNo).append(" and year = ").append(year).append(") v");
            }
        }
    }

    public void getAdgrpPerMonthlyOrWeeklyQueries(int i, String grpIds) {
        LOGGER.info("Adgroup Performance Report has started");
        gleQuery.append("( select  ").append(i).append(" as week_no,")
                .append("  Week, Date_Range,sum(impressions) as gimpressions,sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as  gRevenue,"
                + " avg(avg_pos) as gavg_pos ,")
                .append("  campaign_name,account_id,campaign_id,adgroup_name,adgroup_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,Week,Date_Range,campaign_name,account_id,campaign_id,adgroup_name,adgroup_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos, client_id ,account_id, ")
                .append(" replace(c.campaign_name,' ','_') as campaign_name,c.campaign_id,Replace(x.adgroup_name,' ','_') AS adgroup_name,x.adgroup_id   from ").append(gleseStatsTableName).append(" s , (select distinct(campaign_id),campaign_name from AW_ReportCampaign ) c , ( SELECT DISTINCT(adgroup_id),adgroup_name FROM AW_ReportAdGroup ) x  WHERE  s.adgroup_id = x.adgroup_id  and").append("   s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append(" level_of_detail = 2 ").append(" and ").append("account_id = ")
                .append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) k ) h group by campaign_name,account_id,campaign_id,adgroup_name,adgroup_id)d");

        msnQuery.append("( select  ").append(i).append(" as week_no,")
                .append("  Week, Date_Range,sum(impressions) as mimpressions,sum(clicks) as mclicks,sum(cost) as mcost, sum(CONVERSIONS) as mOrders,sum(total_conv_value) as  mRevenue,"
                        + " avg(avg_pos) as mavg_pos ,")
                .append("  campaign_name,account_id,campaign_id,adgroup_name,adgroup_id from ( select impressions,clicks ,cost , CONVERSIONS ,total_conv_value,(AVG_POS*Impressions) as avg_pos,Week,Date_Range,replace(campaign_name,' ','_') as campaign_name,account_id,campaign_id,adgroup_name,adgroup_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,impressions,clicks, cost, CONVERSIONS, total_conv_value, avg_pos, client_id, account_id, ")
                .append("c.campaign_name as campaign_name, c.campaign_id, x.adgroup_name AS adgroup_name,x.adgroup_id   from ").append(msnseStatsTableName).append(" s , (select distinct(campaign_id),campaign_name from bing_campaign_structure ) c , ( SELECT DISTINCT(adgroup_id),adgroup_name FROM bing_adgroup_structure ) x  WHERE  s.adgroup_id = x.adgroup_id  and").append("   s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append(" level_of_detail = 2 ").append(" and ").append("account_id = ")
                .append(mAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) k ) h group by campaign_name,account_id,campaign_id,adgroup_name,adgroup_id)d");

        yahGemQuery.append("( select  ").append(i).append(" as week_no,")
                .append("  Week, Date_Range,sum(impressions) as yimpressions,sum(clicks) as yclicks,sum(cost) as ycost, sum(CONVERSIONS) as yOrders,sum(total_conv_value) as  yRevenue,"
                        + " avg(avg_pos) as yavg_pos ,")
                .append("  campaign_name,account_id,campaign_id,adgroup_name,adgroup_id from ( select impressions,clicks ,cost , CONVERSIONS ,total_conv_value,(AVG_POS*Impressions) as avg_pos,Week,Date_Range,campaign_name,account_id,campaign_id,adgroup_name,adgroup_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,'").append(dateRange).append("' as Date_Range,impressions,clicks, cost, CONVERSIONS, total_conv_value, avg_pos, client_id, account_id, ")
                .append("c.campaign_name as campaign_name, c.campaign_id, x.adgroup_name AS adgroup_name,x.adgroup_id   from ").append(yahseStatsTableName).append(" s , (select distinct(campaign_id),campaign_name from yahgemini_campaign_structure ) c , ( SELECT DISTINCT(adgroup_id),adgroup_name FROM yahgemini_adgroup_structure ) x  WHERE  s.adgroup_id = x.adgroup_id  and").append("   s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append(" level_of_detail = 2 ").append(" and ").append("account_id = ")
                .append(yahAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) k ) h group by campaign_name,account_id,campaign_id,adgroup_name,adgroup_id)d");

        if (templateInfo.getGoogleAnalytics() != 0) {
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 1 && gAcc_id != 0) {
                gaGleQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,campaign_name,account_id,se_campaign_id,adgroup_name,adgroup_id  ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,campaign_name,account_id,se_campaign_id, adgroup_name,adgroup_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id,replace(a.adgroup_name,' ','_') as adgroup_name,a.adgroup_id from ").append(glegaStatsTableName).append(" g, (select distinct(adgroup_id),adgroup_name from AW_ReportAdGroup ) a").append(" where   g.se_adgroup_id = a.adgroup_id and  ").append(isMonthTillDate)
                        .append(" level_of_detail = 2").append(" and  account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by adgroup_name,account_id,adgroup_id ) )s) h group by campaign_name,account_id,week,date_range,adgroup_id) v");
            } else if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 2 && gAcc_id != 0) {
                gaGleQuery.append("  ( select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,campaign_name,account_id,se_campaign_id,adgroup_name,adgroup_id   ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,campaign_name,account_id,se_campaign_id,adgroup_name,adgroup_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id, replace(a.adgroup_name,' ','_') as adgroup_name,a.adgroup_id   from ").append(glegaStatsTableName).append(" g, (select distinct(adgroup_id),adgroup_name from AW_ReportAdGroup ) a").append(" where  g.se_adgroup_id = a.adgroup_id and  ").append(isMonthTillDate)
                        .append("   account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by adgroup_name,account_id,adgroup_id ) )s)h group by campaign_name,account_id,week,date_range,adgroup_id) v");

            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && mAcc_id != 0) {
//            gaMsnQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,campaign_name,adgroup_name,account_id ")
//                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,campaign_name,adgroup_name,account_id from (  ")
//                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
//                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
//                        .append(" account_id, campaign_name,se_campaign_id,adgroup_name from ").append(msngaStatsTableName).append(" g where level_of_detail = 2").append(" and  account_id in (").append(mAcc_id).append(") and  ")
//                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,adgroup_name,account_id ) )s) h group by campaign_name,account_id,week,date_range,adgroup_name) v");
//            
//                The commented has to be used for generating adgroup performance report when ga is selected, but we are unable to get the bing-ga adgroup level information. Hence cannot group using adgroup_id or adgroup_name. 
                gaMsnQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,campaign_name,account_id ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,campaign_name,account_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id, campaign_name,se_campaign_id from ").append(msngaStatsTableName).append(" g where level_of_detail = 2").append(" and  account_id in (").append(mAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id ) )s) h group by campaign_name,account_id,week,date_range) v");

            } else if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && mAcc_id != 0) {
//            gaMsnQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,campaign_name,adgroup_name,account_id ")
//                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,campaign_name,adgroup_name,account_id from (  ")
//                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
//                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
//                        .append(" account_id, campaign_name,se_campaign_id,adgroup_name from ").append(msngaStatsTableName).append(" g where level_of_detail = 2").append(" and  account_id in (").append(mAcc_id).append(") and  ")
//                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id,adgroup_name ) )s) h group by campaign_name,account_id,week,date_range,adgroup_name) v");

                gaMsnQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,campaign_name,account_id ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,campaign_name,account_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id, campaign_name,se_campaign_id from ").append(msngaStatsTableName).append(" g where level_of_detail = 2").append(" and  account_id in (").append(mAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id ) )s) h group by campaign_name,account_id,week,date_range) v");

            }
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && yahAcc_id != 0) {
                gaYahGemQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,campaign_name,account_id ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,campaign_name,account_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id, campaign_name,se_campaign_id from ").append(yahgaStatsTableName).append(" g where level_of_detail = 2").append(" and  account_id in (").append(yahAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id ) )s) h group by campaign_name,account_id,week,date_range) v");

            } else if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && yahAcc_id != 0) {
                gaYahGemQuery.append(" (  select ").append(i).append(" as week_no,").append("  Week, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,campaign_name,account_id ")
                        .append(" from (      ").append(" select Week, Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,campaign_name,account_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,'").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id, campaign_name,se_campaign_id from ").append(yahgaStatsTableName).append(" g where level_of_detail = 2").append(" and  account_id in (").append(yahAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,account_id ) )s) h group by campaign_name,account_id,week,date_range) v");
            }
        }
    }

    public void getCmpPerfMonthlyOrWeeklyMergeQueries(String tableName) {
        if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && gAcc_id != 0) {
            if (templateInfo.getRevenueType() == 1) { // 1 is for total revenue.
                gaGleMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,gimpressions,gclicks,gcost,gagleOrders,gagleRevenue,campaign_name,se_campaign_id,account_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.gimpressions,d.gclicks,d.gcost,v.gagleOrders,v.gagleRevenue,d.campaign_name,d.campaign_id,d.account_id from ")
                        .append(gleQuery.substring(0, gleQuery.length())).append(" left join ").append(gaGleQuery.substring(0, gaGleQuery.length())).append(" on  v.se_campaign_id = d.campaign_id");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());
            }
            if (templateInfo.getRevenueType() == 2) { // product revenue
                gaGleMergeQuery.append(" insert into ").append(tmpTablename).append("( week_no,week,date_range,gimpressions,gclicks,gcost,gagleOrders,gaglePRevenue,gagleTax,gagleShipping,campaign_name,se_campaign_id,account_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.gimpressions,d.gclicks,d.gcost,v.gagleOrders,v.gaglePRevenue,v.gagleTax,v.gagleShipping,d.campaign_name,d.campaign_id,d.account_id from ")
                        .append(gleQuery.substring(0, gleQuery.length())).append(" left join ").append(gaGleQuery.substring(0, gaGleQuery.length())).append(" on  v.campaign_id = d.campaign_id");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());
            }
        }

        if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 5) && mAcc_id != 0) {
            if (templateInfo.getRevenueType() == 1) {
                gaMsnMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,mimpressions,mclicks,mcost,gamsnorders,gamsnrevenue,campaign_name,se_campaign_id,account_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.mimpressions,d.mclicks,d.mcost,v.gamsnorders,v.gamsnrevenue,d.campaign_name,d.campaign_id,d.account_id from ")
                        .append(msnQuery.substring(0, msnQuery.length())).append(" left join ").append(gaMsnQuery.substring(0, gaMsnQuery.length())).append(" on  v.campaign_name = d.campaign_name"); // ideally on condition should be v.adgroup_id = d.adgroup_id. As we are unable to fetch adgroup information from analytics for bing search engine we are using campaign name.
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());
            }
            if (templateInfo.getRevenueType() == 2) {
                gaMsnMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,mimpressions,mclicks,mcost,gamsnorders,gamsnPRevenue,gamsnTax,gamsnShipping,campaign_name,se_campaign_id,account_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.mimpressions,d.mclicks,d.mcost,v.gamsnorders,v.gamsnPRevenue,v.gamsnTax,v.gamsnShipping,d.campaign_name,d.campaign_id,d.account_id from ")
                        .append(msnQuery.substring(0, msnQuery.length())).append(" left join ").append(gaMsnQuery.substring(0, gaMsnQuery.length())).append(" on  v.campaign_name = d.campaign_name");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());
            }
        }
        if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
            if (templateInfo.getRevenueType() == 1) {
                gaYahGemMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,yimpressions,yclicks,ycost,gayahorders,gayahrevenue,campaign_name,se_campaign_id,account_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.yimpressions,d.yclicks,d.ycost,v.gayahorders,v.gayahrevenue,d.campaign_name,d.campaign_id,d.account_id from ")
                        .append(yahGemQuery.substring(0, yahGemQuery.length())).append(" left join ").append(gaYahGemQuery.substring(0, gaYahGemQuery.length())).append(" on  v.campaign_name = d.campaign_name");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());
            }
            if (templateInfo.getRevenueType() == 2) {
                gaYahGemMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,yimpressions,yclicks,ycost,gayahorders,gayahPRevenue,gayahtax,gayahshipping,campaign_name,se_campaign_id,account_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.yimpressions,d.yclicks,d.ycost,v.gayahorders,v.gayahPRevenue,v.gayahtax,v.gayahshipping,d.campaign_name,d.campaign_id,d.account_id from ")
                        .append(yahGemQuery.substring(0, yahGemQuery.length())).append(" left join ").append(gaYahGemQuery.substring(0, gaYahGemQuery.length())).append(" on  v.campaign_name = d.campaign_name");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());
            }
        }
        if (gAcc_id != 0 && !templateInfo.getAnalyticsSources().contains("1.")) {
            gleMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,gimpressions,gclicks,gcost,gorders,grevenue,campaign_name,account_id,se_campaign_id)")
                    .append("select  d.week_no, d.week, d.date_range, d.gimpressions,d.gclicks,d.gcost,d.gOrders,d.gRevenue,d.campaign_name,d.account_id,d.campaign_id  from ")
                    .append(gleQuery.substring(0, gleQuery.length()));
            autoReportDownloadDao.updateQuery(gleMergeQuery.toString());
        }
        if (mAcc_id != 0 && !templateInfo.getAnalyticsSources().contains("2.")) {
            msnMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,mimpressions,mclicks,mcost,morders,mrevenue,campaign_name,account_id,se_campaign_id)")
                    .append("select  d.week_no, d.week, d.date_range, d.mimpressions,d.mclicks,d.mcost,d.mOrders,d.mRevenue,d.campaign_name,d.account_id,d.campaign_id from ")
                    .append(msnQuery.substring(0, msnQuery.length()));
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());
        }
        if (yahAcc_id != 0 && !templateInfo.getAnalyticsSources().contains("10.")) {
            yahGemMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,campaign_name,account_id,se_campaign_id)")
                    .append("select  d.week_no, d.week, d.date_range, d.yimpressions,d.yclicks,d.ycost,d.yOrders,d.yRevenue,d.campaign_name,d.account_id,d.campaign_id  from ")
                    .append(yahGemQuery.substring(0, yahGemQuery.length()));
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());
        }
    }

    public void getAdgrpPerfMonthlyOrWeeklyMergeQueries(String tableName) {

        if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && gAcc_id != 0) {
            if (templateInfo.getRevenueType() == 1) {
                gaGleMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,gimpressions,gclicks,gcost,gagleOrders,gagleRevenue,campaign_name,se_campaign_id,account_id,adgroup_name,se_adgroup_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.gimpressions,d.gclicks,d.gcost,v.gagleOrders,v.gagleRevenue,d.campaign_name,d.campaign_id,d.account_id,d.adgroup_name,d.adgroup_id from ")
                        .append(gleQuery.substring(0, gleQuery.length())).append(" left join ").append(gaGleQuery.substring(0, gaGleQuery.length())).append(" on  v.adgroup_id = d.adgroup_id");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());
            }
            if (templateInfo.getRevenueType() == 2) { // product revenue
                gaGleMergeQuery.append(" insert into ").append(tmpTablename).append("( week_no,week,date_range,gimpressions,gclicks,gcost,gagleOrders,gaglePRevenue,gagleTax,gagleShipping,campaign_name,se_campaign_id,account_id,adgroup_name,se_adgroup_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.gimpressions,d.gclicks,d.gcost,v.gagleOrders,v.gaglePRevenue,v.gagleTax,v.gagleShipping,d.campaign_name,d.campaign_id,d.account_id,d.adgroup_name,d.adgroup_id  from ")
                        .append(gleQuery.substring(0, gleQuery.length())).append(" left join ").append(gaGleQuery.substring(0, gaGleQuery.length())).append(" on  v.adgroup_id = d.adgroup_id");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());
            }
        }
        if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 5) && mAcc_id != 0) {
            if (templateInfo.getRevenueType() == 1) {
                gaMsnMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,mimpressions,mclicks,mcost,gamsnorders,gamsnrevenue,campaign_name,se_campaign_id,account_id,adgroup_name,se_adgroup_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.mimpressions,d.mclicks,d.mcost,v.gamsnorders,v.gamsnrevenue,d.campaign_name,d.campaign_id,d.account_id,d.adgroup_name,d.adgroup_id from ")
                        .append(msnQuery.substring(0, msnQuery.length())).append(" left join ").append(gaMsnQuery.substring(0, gaMsnQuery.length())).append(" on  v.campaign_name = d.campaign_name"); // ideally on condition should be v.adgroup_id = d.adgroup_id. As we are unable to fetch adgroup information from analytics for bing search engine we are using campaign name.
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());
            }
            if (templateInfo.getRevenueType() == 2) {
                gaMsnMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,mimpressions,mclicks,mcost,gamsnorders,gamsnPRevenue,gamsnTax,gamsnShipping,campaign_name,se_campaign_id,account_id,adgroup_name,se_adgroup_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.mimpressions,d.mclicks,d.mcost,v.gamsnorders,v.gamsnPRevenue,v.gamsnTax,v.gamsnShipping,d.campaign_name,d.campaign_id,d.account_id,d.adgroup_name,d.adgroup_id from ")
                        .append(msnQuery.substring(0, msnQuery.length())).append(" left join ").append(gaMsnQuery.substring(0, gaMsnQuery.length())).append(" on  v.campaign_name = d.campaign_name");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());
            }
        }
        if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
            if (templateInfo.getRevenueType() == 1) {
                gaYahGemMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,yimpressions,yclicks,ycost,gayahorders,gayahrevenue,campaign_name,se_campaign_id,account_id,adgroup_name,se_adgroup_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.yimpressions,d.yclicks,d.ycost,v.gayahorders,v.gayahrevenue,d.campaign_name,d.campaign_id,d.account_id,d.adgroup_name,d.adgroup_id from ")
                        .append(yahGemQuery.substring(0, yahGemQuery.length())).append(" left join ").append(gaYahGemQuery.substring(0, gaYahGemQuery.length())).append(" on  v.campaign_name = d.campaign_name");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());
            }
            if (templateInfo.getRevenueType() == 2) {
                gaYahGemMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,yimpressions,yclicks,ycost,gayahorders,gayahPRevenue,gayahtax,gayahshipping,campaign_name,se_campaign_id,account_id,adgroup_name,se_adgroup_id)")
                        .append("select  d.week_no, d.week, d.date_range, d.yimpressions,d.yclicks,d.ycost,v.gayahorders,v.gayahPRevenue,v.gayahtax,v.gayahshipping,d.campaign_name,d.campaign_id,d.account_id,d.adgroup_name,d.adgroup_id from ")
                        .append(yahGemQuery.substring(0, yahGemQuery.length())).append(" left join ").append(gaYahGemQuery.substring(0, gaYahGemQuery.length())).append(" on  v.campaign_name = d.campaign_name");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());
            }
        }
        if (gAcc_id != 0 && !templateInfo.getAnalyticsSources().contains("1.")) {
            gleMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,gimpressions,gclicks,gcost,gorders,grevenue,campaign_name,account_id,se_campaign_id,adgroup_name,se_adgroup_id)")
                    .append("select  d.week_no, d.week, d.date_range, d.gimpressions,d.gclicks,d.gcost,d.gOrders,d.gRevenue,d.campaign_name,d.account_id,d.campaign_id,d.adgroup_name,d.adgroup_id  from ")
                    .append(gleQuery.substring(0, gleQuery.length()));
            autoReportDownloadDao.updateQuery(gleMergeQuery.toString());
        }
        if (mAcc_id != 0 && !templateInfo.getAnalyticsSources().contains("2.")) {
            msnMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,mimpressions,mclicks,mcost,morders,mrevenue,campaign_name,account_id,se_campaign_id,adgroup_name,se_adgroup_id)")
                    .append("select  d.week_no, d.week, d.date_range, d.mimpressions,d.mclicks,d.mcost,d.mOrders,d.mRevenue,d.campaign_name,d.account_id,d.campaign_id,d.adgroup_name,d.adgroup_id  from ")
                    .append(msnQuery.substring(0, msnQuery.length()));
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());
        }
        if (yahAcc_id != 0 && !templateInfo.getAnalyticsSources().contains("10.")) {
            yahGemMergeQuery.append("insert into ").append(tmpTablename).append("( week_no,week,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,campaign_name,account_id,se_campaign_id,adgroup_name,se_adgroup_id)")
                    .append("select  d.week_no, d.week, d.date_range, d.yimpressions,d.yclicks,d.ycost,d.yOrders,d.yRevenue,d.campaign_name,d.account_id,d.campaign_id,d.adgroup_name,d.adgroup_id  from ")
                    .append(yahGemQuery.substring(0, yahGemQuery.length()));
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());
        }
    }

    public void getConvPerformanceWeeklyQueries(int i) {
        gleQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("'as DateRange, campaign_id se_campaign_id, replace(campaign_name,' ','_') as campaign_name, adgroup_id as se_adgroup_id, adgroup_name, ")
                .append(" keyword_id as se_keyword_id,keyword_text as keyword_name, KEYWORD_MATCH_TYPE as match_type, ")
                .append(" sum(CLICKS) as gclicks, sum(COST) as gcost,sum(IMPRESSIONS) as gimpressions, ")
                .append(" sum(CONVERSIONS) as gorders,avg(QUALITY_SCORE) as quality_score,sum(CONVERSIONVALUE) as grevenue, ")
                .append(" avg(AVERAGE_POSITION) as gavg_pos, ").append(gAcc_id).append(" as account_id ")
                .append("from AW_ReportKeyword where account_id =").append(se_accountId).append(" and day between '").append(fromDate).append("' and '").append(toDate).append("' group by campaign_id,campaign_name, keyword_text COLLATE utf8_bin");

        msnQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("'as DateRange, campaign_id as se_campaign_id, replace(campaign_name,' ','_') as campaign_name, adgroup_id as se_adgroup_id, adgroup_name, ")
                .append(" keyword_id as se_keyword_id, replace(keyword_name,' ','_') as keyword_name, DELIVERED_MATCH_TYPE as match_type, ")
                .append("sum(CLICKS) as mclicks, sum(SPEND) as mcost,sum(IMPRESSIONS) as mimpressions, ")
                .append(" sum(CONVERSIONS) as morders,avg(QUALITY_SCORE) as quality_score,sum(REVENUE) as mrevenue, ")
                .append(" avg(AVG_POS) as mavg_pos, ").append(mAcc_id).append(" as account_id ")
                .append("from Bing_Report_KeywordStats  where se_account_id =").append(se_MsnaccountId).append(" and day between '").append(fromDate).append("' and '").append(toDate).append("' group by campaign_id,campaign_name, keyword_name COLLATE utf8_bin");

        yahGemQuery.append("select ").append(i).append(" as week_no,'").append(dateRange).append("' as DateRange, campaign_id as se_campaign_id, replace(campaign_name,' ','_') as campaign_name, adgroup_id as se_adgroup_id, adgroup_name, ")
                .append("keyword_id as se_keyword_id, replace(keyword_name,' ','_') as keyword_name,MATCH_TYPE as match_type, sum(CLICKS) as yclicks, sum(SPEND) as ycost,sum(IMPRESSIONS) as yimpressions, ")
                .append("sum(CONVERSIONS) as yorders, 0 as yrevenue, 0 as quality_score, avg(AVG_POS) as yavg_pos, ")
                .append(yahAcc_id).append(" as account_id ").append("from Yahoo_Report_KeywordStats  where se_account_id = ").append(se_yahaccountId)
                .append(" and day between '").append(fromDate).append("' and '").append(toDate).append("' group by campaign_id,campaign_name,  keyword_name COLLATE utf8_bin");

        if (templateInfo.getGoogleAnalytics() != 0) {
            List<GoogleAnalyticsInformation> gaDetails = automationTemplateDao.getAnalyticsDefaultData(clientId);
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 1 && gAcc_id != 0) {
                gaGleQuery.append("select ").append(i).append(" as week_no,'").append(dateRange).append("' as DateRange, se_campaign_id, replace(campaign_name,' ','_') as campaign_name, se_adgroup_id, adgroup_name, replace(keyword_name,' ','_') as keyword_name, null as MATCH_TYPE, ")
                        .append("sum(ga_transactions) as gagleorders, sum(ga_revenue) as gaglerevenue , -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, ").append(gAcc_id).append(" as account_id ")
                        //                        .append("from lxr_reportsgakeywordstats where se_account_id =").append(se_accountId).append(" and se_date between '").append(fromDate).append("' and '").append(toDate).append("' and year = ").append(year).append(" group by se_campaign_id, campaign_name,keyword_name ");
                        .append("from lxr_reportsgakeywordstats where profile_id  =").append(gaDetails.get(0).getProfileId()).append(" and client_id = ").append(clientId).append("  and MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 1 and se_date between '").append(fromDate).append("' and '").append(toDate).append("' and year = ").append(year).append(" group by se_campaign_id, campaign_name,keyword_name COLLATE utf8_bin");
            }

            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 2 && gAcc_id != 0) {
                gaGleQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("'as DateRange, se_campaign_id, replace(campaign_name,' ','_') as campaign_name, se_adgroup_id, adgroup_name, replace(keyword_name,' ','_') as keyword_name, null as MATCH_TYPE, ")
                        .append(" -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,")
                        .append(gAcc_id).append(" as account_id ").append(" from lxr_reportsgakeywordstats where profile_id = ").append(gaDetails.get(0).getProfileId()).append(" and client_id = ").append(clientId).append("  and MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 1 and se_date between '").append(fromDate).append("' and '").append(toDate).append("' and year = ")
                        .append(year).append(" group by se_campaign_id, se_adgroup_id,campaign_name, adgroup_name,keyword_name COLLATE utf8_bin");

            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 1 && mAcc_id != 0) {
                gaMsnQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("' as DateRange, se_campaign_id, replace(campaign_name,' ','_') as campaign_name, se_adgroup_id, adgroup_name, replace(keyword_name,' ','_') as keyword_name, MATCH_TYPE, ")
                        .append("sum(ga_transactions) as gamsnorders, sum(ga_revenue) as gamsnrevenue , -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost ,").append(mAcc_id).append(" as account_id ")
                        .append(" from lxr_reportsgakeywordstats where profile_id  =").append(gaDetails.get(0).getProfileId()).append(" and client_id = ").append(clientId).append("  and MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 2 and se_date  between '").append(fromDate).append("' and '").append(toDate).append("' and year = ").append(year).append(" group by se_campaign_id,campaign_name,keyword_name COLLATE utf8_bin");

            }

            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 2 && mAcc_id != 0) {
                gaMsnQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("' as DateRange, se_campaign_id, replace(campaign_name,' ','_') as campaign_name, se_adgroup_id, adgroup_name, replace(keyword_name,' ','_') as keyword_name, MATCH_TYPE, ")
                        .append(" -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,").append(mAcc_id).append(" as account_id ")
                        .append(" from lxr_reportsgakeywordstats where profile_id  =").append(gaDetails.get(0).getProfileId()).append(" and client_id = ").append(clientId).append("  and MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 2 and se_date  between '").append(fromDate).append("' and '").append(toDate).append("' and year = ").append(year).append(" group by se_campaign_id,campaign_name,keyword_name COLLATE utf8_bin");

            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 1 && yahAcc_id != 0) {
                gaYahGemQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("' as DateRange, se_campaign_id, replace(campaign_name,' ','_') as campaign_name, se_adgroup_id, adgroup_name,replace(keyword_name,' ','_') as keyword_name, MATCH_TYPE, ")
                        .append(" sum(ga_transactions) as gayahorders, sum(ga_revenue) as gayahrevenue , -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost ,").append(yahAcc_id).append(" as account_id ")
                        .append(" from lxr_reportsgakeywordstats where profile_id  =").append(gaDetails.get(0).getProfileId()).append(" and client_id = ").append(clientId).append("  and MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 3 and se_date  between '").append(fromDate).append("' and '").append(toDate).append("' and year = ").append(year).append(" group by se_campaign_id,campaign_name,keyword_name COLLATE utf8_bin");

            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 4) && templateInfo.getRevenueType() == 2 && yahAcc_id != 0) {
                gaYahGemQuery.append("select  ").append(i).append(" as week_no,'").append(dateRange).append("' as DateRange, se_campaign_id, replace(campaign_name,' ','_') as campaign_name, se_adgroup_id, adgroup_name,replace(keyword_name,' ','_') as keyword_name, MATCH_TYPE, ")
                        .append("sum(GA_TRANSACTIONS) as gayahorders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, ").append(yahAcc_id).append(" as account_id ")
                        .append(" from lxr_reportsgakeywordstats where profile_id  =").append(gaDetails.get(0).getProfileId()).append(" and client_id = ").append(clientId).append("  and MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 3 and se_date  between '").append(fromDate).append("' and '").append(toDate).append("' and year = ").append(year).append(" group by se_campaign_id,campaign_name,keyword_name COLLATE utf8_bin");

            }
        }
    }

    public void getKeywordPerfMonthlyOrWeeklyMergeQueries(String tableName) {

        gleMergeQuery.append("insert into ").append(tableName)
                .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                .append("date_range,week_no,gIMPRESSIONS,gCLICKS,gCOST,gorders,grevenue,gavg_pos,se_keyword_id, keyword_name , match_type, quality_score) ")
                .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                .append(" DateRange,week_no,gIMPRESSIONS,gCLICKS,gCOST,gorders,grevenue,gavg_pos, se_keyword_id,  keyword_name, match_type, quality_score from (")
                .append(gleQuery).append(" ) gleStats ").append(" ON DUPLICATE KEY Update")
                .append(" se_adgroup_id = gleStats.se_adgroup_id,se_campaign_id = gleStats.se_campaign_id ,SE_KEYWORD_ID = gleStats.SE_KEYWORD_ID, MATCH_TYPE = gleStats.MATCH_TYPE");
        autoReportDownloadDao.updateQuery(gleMergeQuery.toString());

        msnMergeQuery.append("insert into ").append(tableName)
                .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                .append("date_range,week_no,mIMPRESSIONS,mCLICKS,mCOST,morders,mrevenue,mavg_pos,se_keyword_id, keyword_name , match_type, quality_score) ")
                .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                .append(" DateRange,week_no,mIMPRESSIONS,mCLICKS,mCOST,morders,mrevenue,mavg_pos, se_keyword_id,  keyword_name, match_type, quality_score from (")
                .append(msnQuery).append(" ) msnStats ").append(" ON DUPLICATE KEY Update")
                .append(" se_adgroup_id = msnStats.se_adgroup_id,se_campaign_id = msnStats.se_campaign_id ,SE_KEYWORD_ID = msnStats.SE_KEYWORD_ID, MATCH_TYPE = msnStats.MATCH_TYPE");
        autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

        yahGemMergeQuery.append("insert into ").append(tableName)
                .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                .append("date_range,week_no,yIMPRESSIONS,yCLICKS,yCOST,yorders,yrevenue,yavg_pos,se_keyword_id, keyword_name , match_type, quality_score ) ")
                .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                .append(" DateRange,week_no,yIMPRESSIONS,yCLICKS,yCOST,yorders,yrevenue,yavg_pos, se_keyword_id,  keyword_name, match_type, quality_score from (")
                .append(yahGemQuery).append(" ) yahStats ").append(" ON DUPLICATE KEY Update")
                .append(" se_adgroup_id = yahStats.se_adgroup_id,se_campaign_id = yahStats.se_campaign_id ,SE_KEYWORD_ID = yahStats.SE_KEYWORD_ID, MATCH_TYPE = yahStats.MATCH_TYPE");
        autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

        if (templateInfo.getGoogleAnalytics() != 0) {

            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && gAcc_id != 0) {
                if (templateInfo.getRevenueType() == 1) {
                    gaGleMergeQuery.append("insert into ").append(tableName)
                            .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append("date_range,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleorders,gaglerevenue,keyword_name , match_type ) ")
                            .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append(" DateRange,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleorders,gaglerevenue, keyword_name, match_type from (")
                            .append(gaGleQuery).append(" ) gagleStats ").append(" ON DUPLICATE KEY Update")
                            .append(" gagleorders= gagleStats.gagleorders,gaglerevenue= gagleStats.gaglerevenue");
                    autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());
                }
                if (templateInfo.getRevenueType() == 2) {
                    gaGleMergeQuery.append("insert into ").append(tableName)
                            .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append("date_range,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleorders,gaglePRevenue,gagleTax,gagleShipping, keyword_name , match_type ) ")
                            .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append(" DateRange,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleorders,gaglePRevenue,gagleTax,gagleShipping, keyword_name, match_type  from (")
                            .append(gaGleQuery).append(" ) gagleStats ").append(" ON DUPLICATE KEY Update")
                            .append(" gagleorders= gagleStats.gagleorders,gaglePRevenue= gagleStats.gaglePRevenue,gagleTax= gagleStats.gagleTax,gagleShipping= gagleStats.gagleShipping");
                    autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());
                }
            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 5) && mAcc_id != 0) {
                if (templateInfo.getRevenueType() == 1) {
                    gaMsnMergeQuery.append("insert into ").append(tableName)
                            .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append("date_range,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnorders,gamsnrevenue,keyword_name , match_type ) ")
                            .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append(" DateRange,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnorders,gamsnrevenue, keyword_name, match_type from (")
                            .append(gaMsnQuery).append(" ) gamsnStats ").append(" ON DUPLICATE KEY Update")
                            .append(" gamsnorders= gamsnStats.gamsnorders,gamsnrevenue= gamsnStats.gamsnrevenue");
                    autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());
                }
                if (templateInfo.getRevenueType() == 2) {
                    gaMsnMergeQuery.append("insert into ").append(tableName)
                            .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append("date_range,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnorders,gamsnPRevenue,gamsnTax,gamsnShipping, keyword_name , match_type ) ")
                            .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append(" DateRange,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnorders,gamsnPRevenue,gamsnTax,gamsnShipping, keyword_name, match_type  from (")
                            .append(gaMsnQuery).append(" ) gamsnStats ").append(" ON DUPLICATE KEY Update")
                            .append(" gamsnorders= gamsnStats.gamsnorders,gamsnPRevenue= gamsnStats.gamsnPRevenue,gamsnTax= gamsnStats.gamsnTax,gamsnShipping= gamsnStats.gamsnShipping");
                    autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());
                }
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 4) && yahAcc_id != 0) {
                if (templateInfo.getRevenueType() == 1) {
                    gaYahGemMergeQuery.append("insert into ").append(tableName)
                            .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append("date_range,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahrevenue,keyword_name , match_type ) ")
                            .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append(" DateRange,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahrevenue, keyword_name, match_type from (")
                            .append(gaYahGemQuery).append(" ) gayahStats ").append(" ON DUPLICATE KEY Update")
                            .append(" gayahorders= gayahStats.gayahorders,gayahrevenue= gayahStats.gayahrevenue");
                    autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());
                }
                if (templateInfo.getRevenueType() == 2) {
                    gaYahGemMergeQuery.append("insert into ").append(tableName)
                            .append("( account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append("date_range,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahPRevenue,gayahtax,gayahshipping, keyword_name , match_type ) ")
                            .append(" select account_id,campaign_name,se_campaign_id,adgroup_name,se_adgroup_id,")
                            .append(" DateRange,week_no,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahPRevenue,gayahtax,gayahshipping, keyword_name, match_type  from (")
                            .append(gaYahGemQuery).append(" ) gayahStats ").append(" ON DUPLICATE KEY Update")
                            .append(" gayahorders= gayahStats.gayahorders,gayahPRevenue= gayahStats.gayahPRevenue,gayahtax= gayahStats.gayahtax,gayahshipping= gayahStats.gayahshipping");
                    autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());
                }
            }
        }
    }

    public String getDayWisePerformanceReport() {
        try {
            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.DAY_WISE_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            getTillDateWeeklyQueries(tmpTablename, "507", AutoReportConstants.DAY_WISE_REPORT_WEEKLY);

            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);

            finalStatsQuery = "select week_no,date_format(DATE_RANGE,'%d-%b-%Y' )as date_range,SUBSTRING(date_format(DATE_RANGE, '%W') ,1,3) as day," + sqlColumnNames + ",yearoveryear from " + tmpTablename + "  order by week_no asc";
            LOGGER.info("Sem Day Wise Weekly query>>>>>>>>>>" + finalStatsQuery);

            excelGenerator = setDataToExcelGenerator();
            excelGenerator.setReportColNames("Week_no,Date Range,Day," + reportColNames);
            excelGenerator.setReportName(AutoReportConstants.DAY_WISE_REPORT_NAME);
            excelGenerator.setReportType(AutoReportConstants.DAY_WISE_REPORT_WEEKLY);
            return excelGenerator.generateNewXLSXReport();
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
        return "";
    }

    public void getTillDateWeeklyQueries(String tableName, String grpIds, int reportType) {
        seAccId = accountDetailsDao.getAccountSeId(gAcc_id);
        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }

        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }

        gleseStatsTableName = "AW_ReportAdGroup";
        glegaStatsTableName = "lxr_reportsgastats";

        msnseStatsTableName = "Bing_Report_AdgroupStats";
        msngaStatsTableName = "lxr_reportsgastats";

        yahseStatsTableName = "Yahoo_Report_AdgroupStats";
        yahgaStatsTableName = "lxr_reportsgastats";

        getTmpGroupTableSql(tableName);

        gleQuery = new StringBuilder();
        gaGleQuery = new StringBuilder();

        gleYOYQuery = new StringBuilder();
        gaGleYOYQuery = new StringBuilder();

        gleMergeQuery = new StringBuilder();
        gaGleMergeQuery = new StringBuilder();

        gleYOYMergeQuery = new StringBuilder();
        gaGleYOYMergeQuery = new StringBuilder();

        msnQuery = new StringBuilder();
        gaMsnQuery = new StringBuilder();

        msnYOYQuery = new StringBuilder();
        gaMsnYOYQuery = new StringBuilder();

        msnMergeQuery = new StringBuilder();
        gaMsnMergeQuery = new StringBuilder();

        msnYOYMergeQuery = new StringBuilder();
        gaMsnYOYMergeQuery = new StringBuilder();

        yahGemQuery = new StringBuilder();
        gaYahGemQuery = new StringBuilder();

        yahGemYOYQuery = new StringBuilder();
        gaYahGemYOYQuery = new StringBuilder();

        yahGemMergeQuery = new StringBuilder();
        gaYahGemMergeQuery = new StringBuilder();

        yahGemYOYMergeQuery = new StringBuilder();
        gaYahGemYOYMergeQuery = new StringBuilder();

        cal = Calendar.getInstance();
        fcalYOY = Calendar.getInstance();
        if (reportType == AutoReportConstants.DAY_WISE_REPORT_WEEKLY) {
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.WEEK_OF_YEAR, 1);
            cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            fdate = cal.get(Calendar.DATE);
            fyear = cal.get(Calendar.YEAR);
            fmonthNo = cal.get(Calendar.MONTH);
            fmonthName = CommonFunctions.getMonthName(fmonthNo);
//            fromDate = new DateConverter(cal).getOracleDateForm();
            fromDate = new DateConverter(cal).getSQLDateForm1();

            fcalYOY.set(Calendar.MONTH, fmonthNo);
            fcalYOY.set(Calendar.WEEK_OF_YEAR, 1);
            fcalYOY.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            fcalYOY.set(Calendar.YEAR, fyear - 1);
            fcalYOY.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));
//            yoyFromDate = new DateConverter(fcalYOY).getOracleDateForm();
            yoyFromDate = new DateConverter(fcalYOY).getSQLDateForm1();

            cal = Calendar.getInstance();
            if (isCustomDownload()) { // for custom download
                cal.add(Calendar.DATE, -1);
                cal.set(Calendar.DAY_OF_WEEK, weekStart);

                if (cal.getTime().after(Calendar.getInstance().getTime())) {
                    cal.add(Calendar.WEEK_OF_MONTH, -1);
                }
            } else {
                cal.add(Calendar.DATE, -2);
                cal.set(Calendar.DAY_OF_WEEK, weekStart);
            }

            fcalYOY = Calendar.getInstance();
            fcalYOY.set(Calendar.YEAR, fyear - 1);
            fcalYOY.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            fcalYOY.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR));
            fcalYOY.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));

            tdate = cal.get(Calendar.DAY_OF_MONTH);
            tyear = cal.get(Calendar.YEAR);
            tmonthNo = cal.get(Calendar.MONTH);
            tmonthName = CommonFunctions.getMonthName(tmonthNo);
//            toDate = new DateConverter(cal).getOracleDateForm();
            toDate = new DateConverter(cal).getSQLDateForm1();
//            yoyToDate = new DateConverter(fcalYOY).getOracleDateForm();
            yoyToDate = new DateConverter(fcalYOY).getSQLDateForm1();

            year = fyear;
            reportingDate = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
            getDayWiseQueries(grpIds);
        } else {
            if (isCustomDownload()) { // for custom download
                cal.add(Calendar.DATE, -1);
                cal.set(Calendar.DAY_OF_WEEK, weekStart);

                if (cal.getTime().after(Calendar.getInstance().getTime())) {
                    cal.add(Calendar.WEEK_OF_MONTH, -1);
                }
            } else {
                cal.add(Calendar.DATE, -2);
                cal.set(Calendar.DAY_OF_WEEK, weekStart);
            }

            int loopCnt = 0;
            if (reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                loopCnt = 1;
            } else {
                loopCnt = cal.get(Calendar.MONTH) + 1;
            }

            for (int i = 0; i < loopCnt; i++) {
                if (i != 0) {
                    cal.add(Calendar.MONTH, -1);
                }
                weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
                if (reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                }
                cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                fdate = cal.get(Calendar.DATE);
                fyear = cal.get(Calendar.YEAR);
                fmonthNo = cal.get(Calendar.MONTH);
                fmonthName = CommonFunctions.getMonthName(fmonthNo);
                fromDate = new DateConverter(cal).getSQLDateForm1();

                fcalYOY.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                fcalYOY.set(Calendar.YEAR, fyear - 1);
                fcalYOY.set(Calendar.MONTH, fmonthNo);

                yoyfdate = fcalYOY.get(Calendar.DATE);
                yoyfyear = fcalYOY.get(Calendar.YEAR);
                yoyfmonthNo = fcalYOY.get(Calendar.MONTH);
                yoyfmonthName = CommonFunctions.getMonthName(yoyfmonthNo);
                yoyFromDate = new DateConverter(fcalYOY).getSQLDateForm1();

                if (i != 0) {
                    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    fcalYOY.set(Calendar.DATE, fcalYOY.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else {
                    cal = Calendar.getInstance();

                    if (isCustomDownload()) { // for custom download
                        cal.add(Calendar.DATE, -1);
                        cal.set(Calendar.DAY_OF_WEEK, weekStart);
                        if (cal.getTime().after(Calendar.getInstance().getTime())) {
                            cal.add(Calendar.WEEK_OF_MONTH, -1);
                        }
                    } else {
                        cal.add(Calendar.DATE, -2);
                        cal.set(Calendar.DAY_OF_WEEK, weekStart);
                    }
                    fcalYOY = Calendar.getInstance();
                    fcalYOY.set(Calendar.YEAR, fyear - 1);
                    fcalYOY.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                    fcalYOY.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                }

                tdate = cal.get(Calendar.DAY_OF_MONTH);
                tyear = cal.get(Calendar.YEAR);
                tmonthNo = cal.get(Calendar.MONTH);
                tmonthName = CommonFunctions.getMonthName(tmonthNo);
                toDate = new DateConverter(cal).getSQLDateForm1();

                yoytdate = fcalYOY.get(Calendar.DAY_OF_MONTH);
                yoytyear = fcalYOY.get(Calendar.YEAR);
                yoytmonthNo = fcalYOY.get(Calendar.MONTH);
                yoytmonthName = CommonFunctions.getMonthName(yoytmonthNo);
                yoyToDate = new DateConverter(fcalYOY).getSQLDateForm1();

                if (i == 0) {
                    dateRange = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
                } else {
                    dateRange = fmonthName + " " + fdate + "-" + tmonthName + " " + tdate;
                }
                yoyDateRange = yoyfmonthName + " " + yoyfdate + "-" + yoytmonthName + " " + yoytdate;

                durNo = fmonthNo;
                year = fyear;

                if (i == 0) {
                    reportingDate = dateRange;
                    dateRange = fmonthName + " " + fdate + "-" + tmonthName + " " + tdate;
                }
                if (reportType == AutoReportConstants.MONTH_TILL_DATE_REPORT_WEEKLY || reportType == AutoReportConstants.YEAR_TILL_DATE_REPORT_WEEKLY) {
                    getGroupMonthlyOrWeeklyTillDateQueries((i + 1), "RM" + (i + 1), grpIds, reportType);
                }
            }
        }

    }

    public void getDayWiseQueries(String grpIds) {
        try {
            String[] grpIdStr = grpIds.split(",");
            StringBuilder ddw1 = new StringBuilder();
            for (int j = 0; j < grpIdStr.length; j++) {
                ddw1.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id, ").append("adgroup_id,2 as component_level, ").append(grpIdStr[j]).append(" as group_id ").append("  from adgroup_structure where campaign_id in (")
                        .append(" select campaign_id from reports_automation_groups where component_level=1 and ")
                        .append("ACCOUNT_ID = ").append(gAcc_id).append(" and group_id = ").append(grpIdStr[j]).append(")");
            }
            StringBuilder ddw2 = new StringBuilder();
            for (int j = 0; j < grpIdStr.length; j++) {
                ddw2.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id, ").append("adgroup_id,2 as component_level, ").append(grpIdStr[j]).append(" as group_id ").append("  from bing_adgroup_structure where campaign_id in (")
                        .append(" select campaign_id from reports_automation_groups where component_level=1 and ")
                        .append("ACCOUNT_ID = ").append(mAcc_id).append(" and group_id = ").append(grpIdStr[j]).append(")");
            }
            StringBuilder ddw3 = new StringBuilder();
            for (int j = 0; j < grpIdStr.length; j++) {
                ddw3.append(" union ")
                        .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id, ").append("adgroup_id,2 as component_level, ").append(grpIdStr[j]).append(" as group_id ").append("  from yahgemini_adgroup_structure where campaign_id in (")
                        .append(" select campaign_id from reports_automation_groups where component_level=1 and ")
                        .append("ACCOUNT_ID = ").append(yahAcc_id).append(" and group_id = ").append(grpIdStr[j]).append(")");
            }
            gleQuery.append(" SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,GIMPRESSIONS,GCLICKS ,GCOST,GORDERS ,GREVENUE,GAVG_POS,GROUP_NAME,yearoveryear from (")
                    .append(" SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS GIMPRESSIONS,SUM(CLICKS) AS GCLICKS ,SUM(COST) AS GCOST,SUM(ORDERS) AS GORDERS ,")
                    .append(" SUM(TOTAL_CONV_VALUE) AS GREVENUE, sum(AVG_POS) AS GAVG_POS,GROUP_NAME,0 as yearoveryear FROM (SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,")
                    .append(" SUM(orders) AS ORDERS,SUM(TOTAL_CONV_VALUE) AS TOTAL_CONV_VALUE, case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS AVG_POS, GROUP_NAME FROM (  ")
                    .append(" SELECT YEAR,day AS DATE_RANGE, SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,(AVERAGE_POSITION*IMPRESSIONS) as AVG_POS, SUM(").append(orderType).append(") as Orders,SUM(conversionvalue) AS TOTAL_CONV_VALUE, ")
                    .append(" ADGROUP_NAME,ADGROUP_ID FROM ").append(gleseStatsTableName)
                    .append(" WHERE ACCOUNT_ID = ").append(seAccId).append(" and ").append("day between '").append(fromDate).append("' and '").append(toDate).append("'")
                    .append(" and year = ").append(year).append(" GROUP BY YEAR, day ,(AVERAGE_POSITION*IMPRESSIONS), ADGROUP_NAME,ADGROUP_ID) b1 ").append(" join").append(" (   ").append(" select client_id,ACCOUNT_ID,adgroup_id,component_level,grps.group_id,group_name  from  ")
                    .append(" (select client_id,ACCOUNT_ID,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append(" ACCOUNT_ID = ").append(gAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(ddw1.toString())
                    .append("  ) grps ").append(" join  (select group_id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.group_id ").append(" ) ").append(" grpnames  on(grpnames.adgroup_id = b1.adgroup_id) GROUP BY YEAR, DATE_RANGE, GROUP_NAME UNION ALL ")
                    .append(" select ").append(year).append(" as year,DATE_RANGE,0 AS IMPRESSIONS, ")
                    .append(" 0 AS CLICKS , 0 AS COST ,0 AS CONV_ONE_PER_CLICK ,0 as total_conv_value, ")
                    .append(" 0 as avg_pos,group_name from (select a.DATE_RANGE ,507 as group_id ")
                    .append(" from (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE  ")
                    .append(" from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  ")
                    .append("join (select group_name,group_id from lxr_kpi_group_master where group_id in (507)) as v ")
                    .append("on k.group_id=v.group_id ) l")
                    .append(" GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE)m JOIN ")
                    .append(" (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from  ")
                    .append("(select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE  ")
                    .append("from (select a.DATE_RANGE  from ( ")
                    .append("select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE ")
                    .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                    .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days ")
                    .append(" on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

            msnQuery.append(" SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,mIMPRESSIONS,mCLICKS ,mCOST,mORDERS ,mREVENUE,mAVG_POS,GROUP_NAME,yearoveryear from (")
                    .append(" SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS mIMPRESSIONS,SUM(CLICKS) AS mCLICKS ,SUM(COST) AS mCOST,SUM(ORDERS) AS mORDERS ,")
                    .append(" SUM(TOTAL_CONV_VALUE) AS mREVENUE, sum(AVG_POS) AS mAVG_POS,GROUP_NAME,0 as yearoveryear FROM (SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,")
                    .append(" SUM(orders) AS ORDERS,SUM(TOTAL_CONV_VALUE) AS TOTAL_CONV_VALUE, case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS AVG_POS, GROUP_NAME FROM (  ")
                    .append(" SELECT ").append(year).append(" as YEAR,day AS DATE_RANGE, SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(SPEND) AS COST,(AVG_POS*IMPRESSIONS) as AVG_POS, SUM(").append(orderType).append(") as Orders,SUM(REVENUE) AS TOTAL_CONV_VALUE, ")
                    .append(" ADGROUP_NAME,ADGROUP_ID FROM ").append(msnseStatsTableName)
                    .append(" WHERE SE_ACCOUNT_ID = ").append(msnseAccId).append(" and ").append("day between '").append(fromDate).append("' and '").append(toDate).append("'")
                    .append("  GROUP BY YEAR, day ,(AVG_POS*IMPRESSIONS), ADGROUP_NAME,ADGROUP_ID) b1 ").append(" join").append(" (   ").append(" select client_id,ACCOUNT_ID,adgroup_id,component_level,grps.group_id,group_name  from  ")
                    .append(" (select client_id,ACCOUNT_ID,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append(" ACCOUNT_ID = ").append(mAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(ddw2.toString())
                    .append("  ) grps ").append(" join  (select group_id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.group_id ").append(" ) ").append(" grpnames  on(grpnames.adgroup_id = b1.adgroup_id) GROUP BY YEAR, DATE_RANGE, GROUP_NAME UNION ALL ")
                    .append(" select ").append(year).append(" as year,DATE_RANGE,0 AS IMPRESSIONS, ")
                    .append(" 0 AS CLICKS , 0 AS COST ,0 AS CONV_ONE_PER_CLICK ,0 as total_conv_value, ")
                    .append(" 0 as avg_pos,group_name from (select a.DATE_RANGE ,507 as group_id ")
                    .append(" from (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE  ")
                    .append(" from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  ")
                    .append("join (select group_name,group_id from lxr_kpi_group_master where group_id in (507)) as v ")
                    .append("on k.group_id=v.group_id ) l")
                    .append(" GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE)m JOIN ")
                    .append(" (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from  ")
                    .append("(select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE  ")
                    .append("from (select a.DATE_RANGE  from ( ")
                    .append("select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE ")
                    .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                    .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days ")
                    .append(" on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

            yahGemQuery.append(" SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,yIMPRESSIONS,yCLICKS ,yCOST,yORDERS ,yREVENUE,yAVG_POS,GROUP_NAME,yearoveryear from (")
                    .append(" SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS yIMPRESSIONS,SUM(CLICKS) AS yCLICKS ,SUM(COST) AS yCOST,SUM(ORDERS) AS yORDERS ,")
                    .append(" SUM(TOTAL_CONV_VALUE) AS yREVENUE, sum(AVG_POS) AS yAVG_POS,GROUP_NAME,0 as yearoveryear FROM (SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,")
                    .append(" SUM(orders) AS ORDERS,SUM(TOTAL_CONV_VALUE) AS TOTAL_CONV_VALUE, case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS AVG_POS, GROUP_NAME FROM (  ")
                    .append(" SELECT ").append(year).append(" as YEAR,day AS DATE_RANGE, SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(SPEND) AS COST,(AVG_POS*IMPRESSIONS) as AVG_POS, SUM(").append(orderType).append(") as Orders,SUM(COST_PER_CONVERSION) AS TOTAL_CONV_VALUE, ")
                    .append(" ADGROUP_NAME,ADGROUP_ID FROM ").append(yahseStatsTableName)
                    .append(" WHERE SE_ACCOUNT_ID = ").append(yahseAccId).append(" and ").append("day between '").append(fromDate).append("' and '").append(toDate).append("'")
                    .append("  GROUP BY YEAR, day ,(AVG_POS*IMPRESSIONS), ADGROUP_NAME,ADGROUP_ID) b1 ").append(" join").append(" (   ").append(" select client_id,ACCOUNT_ID,adgroup_id,component_level,grps.group_id,group_name  from  ")
                    .append(" (select client_id,ACCOUNT_ID,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append(" ACCOUNT_ID = ").append(yahAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(ddw3.toString())
                    .append("  ) grps ").append(" join  (select group_id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.group_id ").append(" ) ").append(" grpnames  on(grpnames.adgroup_id = b1.adgroup_id) GROUP BY YEAR, DATE_RANGE, GROUP_NAME UNION ALL ")
                    .append(" select ").append(year).append(" as year,DATE_RANGE,0 AS IMPRESSIONS, ")
                    .append(" 0 AS CLICKS , 0 AS COST ,0 AS CONV_ONE_PER_CLICK ,0 as total_conv_value, ")
                    .append(" 0 as avg_pos,group_name from (select a.DATE_RANGE ,507 as group_id ")
                    .append(" from (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE  ")
                    .append(" from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  ")
                    .append("join (select group_name,group_id from lxr_kpi_group_master where group_id in (507)) as v ")
                    .append("on k.group_id=v.group_id ) l")
                    .append(" GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE)m JOIN ")
                    .append(" (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from  ")
                    .append("(select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE  ")
                    .append("from (select a.DATE_RANGE  from ( ")
                    .append("select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE ")
                    .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                    .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days ")
                    .append(" on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

            gleYOYQuery.append(" SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,GIMPRESSIONS,GCLICKS ,GCOST,GORDERS ,GREVENUE,GAVG_POS,GROUP_NAME,yearoveryear from (")
                    .append(" SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS GIMPRESSIONS,SUM(CLICKS) AS GCLICKS ,SUM(COST) AS GCOST,SUM(ORDERS) AS GORDERS ,")
                    .append(" SUM(TOTAL_CONV_VALUE) AS GREVENUE, sum(AVG_POS) AS GAVG_POS,GROUP_NAME, 1 as yearoveryear FROM (SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,")
                    .append(" SUM(orders) AS ORDERS,SUM(TOTAL_CONV_VALUE) AS TOTAL_CONV_VALUE, case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS AVG_POS, GROUP_NAME FROM (  ")
                    .append(" SELECT YEAR,day AS DATE_RANGE, SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,(AVERAGE_POSITION*IMPRESSIONS) as AVG_POS, SUM(").append(orderType).append(") as Orders,SUM(conversionvalue) AS TOTAL_CONV_VALUE, ")
                    .append(" ADGROUP_NAME,ADGROUP_ID FROM ").append(gleseStatsTableName)
                    .append(" WHERE ACCOUNT_ID = ").append(seAccId).append(" and ").append("day between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                    .append(" and year = ").append(year - 1).append(" GROUP BY YEAR, day ,(AVERAGE_POSITION*IMPRESSIONS), ADGROUP_NAME,ADGROUP_ID) b1 ").append(" join").append(" (   ").append(" select client_id,ACCOUNT_ID,adgroup_id,component_level,grps.group_id,group_name  from  ")
                    .append(" (select client_id,ACCOUNT_ID,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append(" ACCOUNT_ID = ").append(gAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(ddw1.toString())
                    .append("  ) grps ").append(" join  (select group_id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.group_id ").append(" ) ").append(" grpnames  on(grpnames.adgroup_id = b1.adgroup_id) GROUP BY YEAR, DATE_RANGE, GROUP_NAME UNION ALL ")
                    .append(" select ").append(year - 1).append(" as year,DATE_RANGE,0 AS IMPRESSIONS, ")
                    .append(" 0 AS CLICKS , 0 AS COST ,0 AS CONV_ONE_PER_CLICK ,0 as total_conv_value, ")
                    .append(" 0 as avg_pos,group_name from (select a.DATE_RANGE ,507 as group_id ")
                    .append(" from (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE  ")
                    .append(" from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  ")
                    .append("join (select group_name,group_id from lxr_kpi_group_master where group_id in (507)) as v ")
                    .append("on k.group_id=v.group_id ) l")
                    .append(" GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE)m JOIN ")
                    .append(" (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from  ")
                    .append("(select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE  ")
                    .append("from (select a.DATE_RANGE  from ( ")
                    .append("select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE ")
                    .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                    .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days ")
                    .append(" on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

            msnYOYQuery.append(" SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,mIMPRESSIONS,mCLICKS ,mCOST,mORDERS ,mREVENUE,mAVG_POS,GROUP_NAME,yearoveryear from (")
                    .append(" SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS mIMPRESSIONS,SUM(CLICKS) AS mCLICKS ,SUM(COST) AS mCOST,SUM(ORDERS) AS mORDERS ,")
                    .append(" SUM(TOTAL_CONV_VALUE) AS mREVENUE, sum(AVG_POS) AS mAVG_POS,GROUP_NAME,1 as yearoveryear FROM (SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,")
                    .append(" SUM(orders) AS ORDERS,SUM(TOTAL_CONV_VALUE) AS TOTAL_CONV_VALUE, case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS AVG_POS, GROUP_NAME FROM (  ")
                    .append(" SELECT ").append(year - 1).append(" as YEAR,day AS DATE_RANGE, SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(SPEND) AS COST,(AVG_POS*IMPRESSIONS) as AVG_POS, SUM(").append(orderType).append(") as Orders,SUM(REVENUE) AS TOTAL_CONV_VALUE, ")
                    .append(" ADGROUP_NAME,ADGROUP_ID FROM ").append(msnseStatsTableName)
                    .append(" WHERE SE_ACCOUNT_ID = ").append(msnseAccId).append(" and ").append("day between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                    .append("  GROUP BY YEAR, day ,(AVG_POS*IMPRESSIONS), ADGROUP_NAME,ADGROUP_ID) b1 ").append(" join").append(" (   ").append(" select client_id,ACCOUNT_ID,adgroup_id,component_level,grps.group_id,group_name  from  ")
                    .append(" (select client_id,ACCOUNT_ID,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append(" ACCOUNT_ID = ").append(mAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(ddw2.toString())
                    .append("  ) grps ").append(" join  (select group_id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.group_id ").append(" ) ").append(" grpnames  on(grpnames.adgroup_id = b1.adgroup_id) GROUP BY YEAR, DATE_RANGE, GROUP_NAME UNION ALL ")
                    .append(" select ").append(year - 1).append(" as year,DATE_RANGE,0 AS IMPRESSIONS, ")
                    .append(" 0 AS CLICKS , 0 AS COST ,0 AS CONV_ONE_PER_CLICK ,0 as total_conv_value, ")
                    .append(" 0 as avg_pos,group_name from (select a.DATE_RANGE ,507 as group_id ")
                    .append(" from (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE  ")
                    .append(" from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  ")
                    .append("join (select group_name,group_id from lxr_kpi_group_master where group_id in (507)) as v ")
                    .append("on k.group_id=v.group_id ) l")
                    .append(" GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE)m JOIN ")
                    .append(" (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from  ")
                    .append("(select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE  ")
                    .append("from (select a.DATE_RANGE  from ( ")
                    .append("select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE ")
                    .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                    .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days ")
                    .append(" on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

            yahGemYOYQuery.append(" SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,yIMPRESSIONS,yCLICKS ,yCOST,yORDERS ,yREVENUE,yAVG_POS,GROUP_NAME,yearoveryear from (")
                    .append(" SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS yIMPRESSIONS,SUM(CLICKS) AS yCLICKS ,SUM(COST) AS yCOST,SUM(ORDERS) AS yORDERS ,")
                    .append(" SUM(TOTAL_CONV_VALUE) AS yREVENUE, sum(AVG_POS) AS yAVG_POS,GROUP_NAME,1 as yearoveryear FROM (SELECT YEAR,DATE_RANGE,SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(COST) AS COST,")
                    .append(" SUM(orders) AS ORDERS,SUM(TOTAL_CONV_VALUE) AS TOTAL_CONV_VALUE, case sum(impressions) when 0 then 0 else (sum(AVG_POS)/sum(Impressions)) end AS AVG_POS, GROUP_NAME FROM (  ")
                    .append(" SELECT ").append(year - 1).append(" as YEAR,day AS DATE_RANGE, SUM(IMPRESSIONS) AS IMPRESSIONS,SUM(CLICKS) AS CLICKS ,SUM(SPEND) AS COST,(AVG_POS*IMPRESSIONS) as AVG_POS, SUM(").append(orderType).append(") as Orders,SUM(COST_PER_CONVERSION) AS TOTAL_CONV_VALUE, ")
                    .append(" ADGROUP_NAME,ADGROUP_ID FROM ").append(yahseStatsTableName)
                    .append(" WHERE SE_ACCOUNT_ID = ").append(yahseAccId).append(" and ").append("day between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                    .append("  GROUP BY YEAR, day ,(AVG_POS*IMPRESSIONS), ADGROUP_NAME,ADGROUP_ID) b1 ").append(" join").append(" (   ").append(" select client_id,ACCOUNT_ID,adgroup_id,component_level,grps.group_id,group_name  from  ")
                    .append(" (select client_id,ACCOUNT_ID,adgroup_id,component_level,group_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                    .append(" ACCOUNT_ID = ").append(yahAcc_id).append(" and component_level = 2 and group_id in (").append(grpIds).append(" ) ").append(ddw3.toString())
                    .append("  ) grps ").append(" join  (select group_id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                    .append(" on grps.group_id = mstr.group_id ").append(" ) ").append(" grpnames  on(grpnames.adgroup_id = b1.adgroup_id) GROUP BY YEAR, DATE_RANGE, GROUP_NAME UNION ALL ")
                    .append(" select ").append(year - 1).append(" as year,DATE_RANGE,0 AS IMPRESSIONS, ")
                    .append(" 0 AS CLICKS , 0 AS COST ,0 AS CONV_ONE_PER_CLICK ,0 as total_conv_value, ")
                    .append(" 0 as avg_pos,group_name from (select a.DATE_RANGE ,507 as group_id ")
                    .append(" from (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE  ")
                    .append(" from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append(" cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  ")
                    .append("join (select group_name,group_id from lxr_kpi_group_master where group_id in (507)) as v ")
                    .append("on k.group_id=v.group_id ) l")
                    .append(" GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE)m JOIN ")
                    .append(" (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from  ")
                    .append("(select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE  ")
                    .append("from (select a.DATE_RANGE  from ( ")
                    .append("select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE ")
                    .append("from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b ")
                    .append("cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c ")
                    .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                    .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days ")
                    .append(" on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

            if (templateInfo.getGoogleAnalytics() != 0) {
                if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && gAcc_id != 0) {

                    gaGleQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gagleOrders,gagleRevenue,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gagleOrders,gagleRevenue,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gagleOrders , SUM(GA_REVENUE) AS gagleRevenue,Group_name,0 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , SUM(GA_REVENUE) AS GA_REVENUE,CLIENT_ID,SE_CAMPAIGN_ID FROM ")
                            .append(glegaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" MEDIUM_TYPE=1 and SEARCH_ENGINE_ID =1 and ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            //                            .append(" and year = ").append(year).append("  group by se_date,se_campaign_id,year,account_id ) b1 join (select * from(select grps.* from ")
                            .append(" and year = ").append(year).append("  group by se_date,se_campaign_id,year ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and grpnames.campaign_id = b1.se_campaign_id )) group by year,Date_Range,group_name")
                            .append(" UNION ALL select ").append(year).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,  ")
                            .append(" 0 AS GA_REVENUE,group_name, 0 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from ")
                            .append(" (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaGleYOYQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gagleOrders,gagleRevenue,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gagleOrders,gagleRevenue,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gagleOrders , SUM(GA_REVENUE) AS gagleRevenue,Group_name,1 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , SUM(GA_REVENUE) AS GA_REVENUE,CLIENT_ID,SE_CAMPAIGN_ID FROM ")
                            .append(glegaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append("  MEDIUM_TYPE=1 and SEARCH_ENGINE_ID =1 and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append(" and year = ").append(year - 1).append("  group by se_date,se_campaign_id,year ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and grpnames.campaign_id = b1.se_campaign_id )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year - 1).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,   ")
                            .append("0 AS GA_REVENUE,group_name, 1 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all  ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  join (select group_name,group_id from  ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE  )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaMsnQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gamsnOrders,gamsnRevenue,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gamsnOrders,gamsnRevenue,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gamsnOrders , SUM(GA_REVENUE) AS gamsnRevenue,Group_name,0 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , SUM(GA_REVENUE) AS GA_REVENUE,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(msngaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append("  MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 2 and ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" and year = ").append(year).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name")
                            .append(" UNION ALL select ").append(year).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,  ")
                            .append(" 0 AS GA_REVENUE,group_name, 0 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from ")
                            .append(" (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaMsnYOYQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gamsnOrders,gamsnRevenue,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gamsnOrders,gamsnRevenue,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gamsnOrders , SUM(GA_REVENUE) AS gamsnRevenue,Group_name,1 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , SUM(GA_REVENUE) AS GA_REVENUE,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(msngaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            .append("  MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 2 and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append(" and year = ").append(year - 1).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name")
                            .append(" UNION ALL select ").append(year - 1).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,  ")
                            .append(" 0 AS GA_REVENUE,group_name, 1 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from ")
                            .append(" (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaYahGemQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gayahOrders,gayahRevenue,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gayahOrders,gayahRevenue,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gayahOrders , SUM(GA_REVENUE) AS gayahRevenue,Group_name,0 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , SUM(GA_REVENUE) AS GA_REVENUE,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(yahgaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append("  MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 3 and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" and year = ").append(year).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name")
                            .append(" UNION ALL select ").append(year).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,  ")
                            .append(" 0 AS GA_REVENUE,group_name, 0 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from ")
                            .append(" (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaYahGemYOYQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gayahOrders,gayahRevenue,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gayahOrders,gayahRevenue,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gayahOrders , SUM(GA_REVENUE) AS gayahRevenue,Group_name,1 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , SUM(GA_REVENUE) AS GA_REVENUE,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(yahgaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append("  MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 3 and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append(" and year = ").append(year - 1).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name")
                            .append(" UNION ALL select ").append(year - 1).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,  ")
                            .append(" 0 AS GA_REVENUE,group_name, 1 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from ")
                            .append(" (select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                } else if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && gAcc_id != 0) {

                    gaGleQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gagleOrders,gaglePRevenue,gagleTax,gagleShipping,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gagleOrders,gaglePRevenue,gagleTax,gagleShipping,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gagleOrders , sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,Group_name,0 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX, sum(GA_SHIPPING) as GA_SHIPPING,CLIENT_ID,SE_CAMPAIGN_ID FROM ")
                            .append(glegaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append("   MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 1 and ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" and year = ").append(year).append("  group by se_date,se_campaign_id,year ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and grpnames.campaign_id = b1.se_campaign_id )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,   ")
                            .append("0 AS GA_PRODUCT_REVENUE,0 as GA_TAX,0 as GA_SHIPPING,group_name, 0 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from  ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from  ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all  ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE   )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaGleYOYQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gagleOrders,gaglePRevenue,gagleTax,gagleShipping,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gagleOrders,gaglePRevenue,gagleTax,gagleShipping,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gagleOrders , sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,Group_name,1 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX, sum(GA_SHIPPING) as GA_SHIPPING,CLIENT_ID,SE_CAMPAIGN_ID FROM ")
                            .append(glegaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(gAcc_id).append(") and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append("  MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 1 and ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append(" and year = ").append(year - 1).append("  group by se_date,se_campaign_id,year ) b1 join (select * from(select grps.* from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and grpnames.campaign_id = b1.se_campaign_id )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year - 1).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,  ")
                            .append("0 AS GA_PRODUCT_REVENUE,0 as GA_TAX,0 as GA_SHIPPING,group_name, 1 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from  ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from  ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaMsnQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gamsnOrders,gamsnPRevenue,gamsnTax,gamsnShipping,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gamsnOrders,gamsnPRevenue,gamsnTax,gamsnShipping,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gamsnOrders , sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,Group_name,0 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX, sum(GA_SHIPPING) as GA_SHIPPING,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(msngaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 2 and ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" and year = ").append(year).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,   ")
                            .append("0 AS GA_PRODUCT_REVENUE,0 as GA_TAX,0 as GA_SHIPPING,group_name, 0 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from  ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from  ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all  ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE   )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaMsnYOYQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gamsnOrders,gamsnPRevenue,gamsnTax,gamsnShipping,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gamsnOrders,gamsnPRevenue,gamsnTax,gamsnShipping,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gamsnOrders , sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,Group_name,1 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX, sum(GA_SHIPPING) as GA_SHIPPING,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(msngaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(mAcc_id).append(") and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(toDate).append("'")
                            .append(" MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 2 and ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append(" and year = ").append(year - 1).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yoyToDate).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year - 1).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,   ")
                            .append("0 AS GA_PRODUCT_REVENUE,0 as GA_TAX,0 as GA_SHIPPING,group_name, 1 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from  ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from  ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all  ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE   )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaYahGemQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gayahOrders,gayahPRevenue,gayahTax,gayahShipping,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gayahOrders,gayahPRevenue,gayahTax,gayahShipping,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gayahOrders , sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,Group_name,0 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX, sum(GA_SHIPPING) as GA_SHIPPING,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(yahgaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 3 and  ").append("se_date between '").append(fromDate).append("' and '").append(toDate).append("'")
                            .append(" and year = ").append(year).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,   ")
                            .append("0 AS GA_PRODUCT_REVENUE,0 as GA_TAX,0 as GA_SHIPPING,group_name, 0 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from  ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from  ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all  ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE   )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(fromDate).append("' and '").append(toDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                    gaYahGemYOYQuery.append("SELECT WEEK_NO,week,YEAR,m.DATE_RANGE,-1000 AS GAIMPRESSIONS,-1000 AS GACLICKS,-1000 AS GACOST,gayahOrders,gayahPRevenue,gayahTax,gayahShipping,GROUP_NAME,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,gayahOrders,gayahPRevenue,gayahTax,gayahShipping,Group_name,yearoveryear from(")
                            .append("SELECT YEAR, DATE_RANGE,SUM(GA_TRANSACTIONS) AS gayahOrders , sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,Group_name,1 as yearoveryear FROM ( (")
                            .append("SELECT  YEAR,SE_DATE AS DATE_RANGE,SUM(GA_TRANSACTIONS) AS GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX, sum(GA_SHIPPING) as GA_SHIPPING,CLIENT_ID,CAMPAIGN_NAME FROM ")
                            .append(yahgaStatsTableName).append(" where level_of_detail = 2 and").append(" client_id =").append(clientId).append(" and ")
                            //                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append("se_date between '").append(yoyFromDate).append("' and '").append(toDate).append("'")
                            .append(" MEDIUM_TYPE = 1 and SEARCH_ENGINE_ID = 3 and ").append("se_date between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("'")
                            .append(" and year = ").append(year - 1).append("  group by se_date,CAMPAIGN_NAME,year ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name  from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(clientId).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.group_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.CLIENT_ID = b1.CLIENT_ID and upper(grpnames.campaign_name) = upper(b1.campaign_name)  )) group by year,Date_Range,group_name ")
                            .append(" UNION ALL select ").append(year - 1).append(" as year,DATE_RANGE,0 AS GA_TRANSACTIONS,   ")
                            .append("0 AS GA_PRODUCT_REVENUE,0 as GA_TAX,0 as GA_SHIPPING,group_name, 1 as yearoveryear from (select a.DATE_RANGE ,507 as group_id  from  ")
                            .append("(select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE   from  ")
                            .append("(select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all ")
                            .append("select 6 union all select 7 union all select 8 union all select 9) as a  cross join (select 0 as a union all select 1  ")
                            .append("union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7  ")
                            .append("union all select 8 union all select 9) as b  cross join (select 0 as a union all select 1 union all select 2 union all  ")
                            .append("select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all  ")
                            .append("select 9) as c  ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("') as k  join (select group_name,group_id from ")
                            .append("lxr_kpi_group_master where group_id in (507)) as v on k.group_id=v.group_id )l  ")
                            .append("GROUP BY  YEAR, DATE_RANGE, GROUP_NAME ORDER BY DATE_RANGE   )m JOIN ")
                            .append("  (select week_NO,concat('RD',week_NO) as week , DATE_RANGE from ")
                            .append(" (select @rownum /*'*/:=/*'*/ @rownum + 1 AS week_NO,DATE_RANGE")
                            .append(" from (select a.DATE_RANGE from ( ")
                            .append(" select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as DATE_RANGE")
                            .append("  from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n")
                            .append("  cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n")
                            .append(" ) a where a.DATE_RANGE between '").append(yoyFromDate).append("' and '").append(yoyToDate).append("' ")
                            .append(" order by DATE_RANGE) t1,(SELECT @rownum /*'*/:=/*'*/ 0) AS r)v)days on (m.DATE_RANGE=days.DATE_RANGE)  union all ");

                }
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        }
    }

    public String getMonthlyPerformanceReport() {

        try {
            String grpIds = "504";
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.PERFORMANCE_SUMMARY_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;

            getMonthlyQueries(tmpTablename, grpIds, AutoReportConstants.STANDARD_REPORT, false);
            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);

            String basicTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by week_no  desc";
            finalStatsQuery = "select date_range," + sqlColumnNames + " from " + tmpTablename + " where yearoveryear = 0 order by week_no desc";
            LOGGER.info("Sem Monthly query>>>>>>>>>>" + finalStatsQuery);
            finalTotals = new ArrayList();
            finalTotals = autoReportDownloadDao.loadObjectsForTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);

            excelGenerator = setDataToExcelGenerator();
            if (yearOverYear == true) {
                excelGenerator.setPrevYear(false);
            }
            excelGenerator.setReportColNames("Date Range," + reportColNames);
            excelGenerator.setReportName(AutoReportConstants.SEM_MONTHLY_REPORT);
            excelGenerator.setIsMonthlyReport(1);
            excelGenerator.setReportType(AutoReportConstants.PERFORMANCE_SUMMARY_REPORT_MONTHLY);
            excelGenerator.setLoopCnt(monthlyLoopCnt - 2);
            excelGenerator.generateNewXLSXReport();

            //for year over year report
            if (yearOverYear == true) {
                finalStatsQuery = "select date_range,week_no," + sqlColumnNames + ",yearoveryear from " + tmpTablename + "  order by week_no asc";//new code
                excelGenerator.setSql(finalStatsQuery);
                LOGGER.info("Sem Monthly query>>>>>>>>>>" + finalStatsQuery);
                excelGenerator.setYearOverYear(yearOverYear);
                excelGenerator.generateNewXLSXReport();
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
        return "";
    }

    public void getMonthlyQueries(String tableName, String grpIds, int reportType, boolean isYoy) {
        seAccId = accountDetailsDao.getAccountSeId(gAcc_id);
        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }
        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }
        if (fbAcc_id != 0) { // TODO : already we got facebook advert account id just get it no need to call it again.
            fbseAccId = accountDetailsDao.getAccountSeId(fbAcc_id);
        }

        gleseStatsTableName = "ne_autoreportsmonthlysestats";
        glegaStatsTableName = "ne_autoreportsmonthlygastats";
        msnseStatsTableName = "ne_autoreportsmonthlybingsestat";
        msngaStatsTableName = "ne_autoreportsmonthlybinggastats";
        yahseStatsTableName = "ne_autoreportsyahmonthlysestats";
        yahgaStatsTableName = "ne_autoreportsyahmonthlygastats";
        durCondition = "month_no";
        isMonthTillDate = " IS_MONTH_TILL_DATE = 0 and ";

        if (fbAcc_id != 0 && grpIds == "509") {
            getFacebookTmpTableSql(tmpTablename);
        } else if (grpIds == "503") {
            getTmpGroupWoWTableSql(tmpTablename);
        } else if ("508".equals(grpIds)) {
            getAmazonTmpTableSql(tmpTablename);
        } else {
            getTmpGroupTableSql(tmpTablename);// Query for creating tmptable
        }

        gleQuery = new StringBuilder();
        gaGleQuery = new StringBuilder();

        fbQuery = new StringBuilder();
        fbMergeQuery = new StringBuilder();
        fbMomMergeQuery = new StringBuilder();
        fbMomQuery = new StringBuilder();
        totalMomQuery = new StringBuilder();

        gleYOYQuery = new StringBuilder();
        gaGleYOYQuery = new StringBuilder();

        gleMergeQuery = new StringBuilder();
        gaGleMergeQuery = new StringBuilder();

        gleYOYMergeQuery = new StringBuilder();
        gaGleYOYMergeQuery = new StringBuilder();

        msnQuery = new StringBuilder();
        gaMsnQuery = new StringBuilder();
        msnYOYQuery = new StringBuilder();
        gaMsnYOYQuery = new StringBuilder();
        msnMergeQuery = new StringBuilder();
        gaMsnMergeQuery = new StringBuilder();
        msnYOYMergeQuery = new StringBuilder();
        gaMsnYOYMergeQuery = new StringBuilder();

        yahGemQuery = new StringBuilder();
        gaYahGemQuery = new StringBuilder();

        yahGemYOYQuery = new StringBuilder();
        gaYahGemYOYQuery = new StringBuilder();

        yahGemMergeQuery = new StringBuilder();
        gaYahGemMergeQuery = new StringBuilder();

        yahGemYOYMergeQuery = new StringBuilder();
        gaYahGemYOYMergeQuery = new StringBuilder();

        amzQuery = new StringBuilder();
        amzSPQuery = new StringBuilder();
        amzHSAQuery = new StringBuilder();
        amzMergeQuery = new StringBuilder();
        amzSPMergeQuery = new StringBuilder();
        amzHSAMergeQuery = new StringBuilder();

        cal = Calendar.getInstance();
        fcalYOY = Calendar.getInstance();
        fcalYOY.add(Calendar.MONTH, -1);
        cal.add(Calendar.MONTH, -1);

        int loopCnt = 0;
        if (reportType == CommonConstants.CAMPAIGN || reportType == CommonConstants.ADGROUP
                || reportType == AutoReportConstants.CONVERTING_KEYWORD || reportType == CommonConstants.FACEBOOK) {
            loopCnt = 2;
        } else {
            loopCnt = monthlyLoopCnt;
        }

        if (isYoy) {
            loopCnt = cal.get(Calendar.MONTH) + 2;
        }
        for (int i = 1; i < loopCnt; i++) {
            if (i != 1) {
                cal.add(Calendar.MONTH, -1);
            }
            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
            cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            fdate = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            fyear = cal.get(Calendar.YEAR);
            fmonthNo = cal.get(Calendar.MONTH);
            fmonthName = CommonFunctions.getMonthName(fmonthNo);
            if (fbAcc_id != 0) {
                fbFromDate = (simpleDateFormat.format(cal.getTime()));
            }
            fromDate = new DateConverter(cal).getOracleDateForm();

            fcalYOY.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            fcalYOY.set(Calendar.YEAR, fyear - 1);
            fcalYOY.set(Calendar.MONTH, fmonthNo);

            yoyfdate = fcalYOY.get(Calendar.DATE);
            yoyfyear = fcalYOY.get(Calendar.YEAR);
            yoyfmonthNo = fcalYOY.get(Calendar.MONTH);
            yoyfmonthName = CommonFunctions.getMonthName(yoyfmonthNo);

            yoyFromDate = new DateConverter(fcalYOY).getOracleDateForm();

            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            fcalYOY.set(Calendar.DATE, fcalYOY.getActualMaximum(Calendar.DAY_OF_MONTH));

            tdate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            tyear = cal.get(Calendar.YEAR);
            tmonthNo = cal.get(Calendar.MONTH);
            tmonthName = CommonFunctions.getMonthName(tmonthNo);
            if (fbAcc_id != 0) {
                fbToDate = (simpleDateFormat.format(cal.getTime()));
            }
            toDate = new DateConverter(cal).getOracleDateForm();

            yoytdate = fcalYOY.getActualMaximum(Calendar.DAY_OF_MONTH);
            yoytyear = fcalYOY.get(Calendar.YEAR);
            yoytmonthNo = fcalYOY.get(Calendar.MONTH);
            yoytmonthName = CommonFunctions.getMonthName(yoytmonthNo);
            yoyToDate = new DateConverter(fcalYOY).getOracleDateForm();

            dateRange = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
            yoyDateRange = yoyfmonthName + " " + yoyfdate + "," + " " + yoyfyear + "-" + yoytmonthName + " " + yoytdate + "," + " " + yoytyear;
            durNo = fmonthNo;
            year = fyear;
            if (i == 1) {
                reportingDate = dateRange;
            }
            int mthNo = 0;
            if (isYoy) {
                mthNo = durNo + 1;
            } else {
                mthNo = i;
            }
            if (reportType == AutoReportConstants.STANDARD_REPORT) {
                getGroupMonthlyOrWeeklyQueries(mthNo, grpIds, reportType);
            } else if (isCustom == false && reportType == AutoReportConstants.GROUP_REPORT) { //For Brand & NonBrand Report
                getGroupMonthlyOrWeeklyQueries(mthNo, grpIds, reportType);
            } else if (isCustom == true && reportType == AutoReportConstants.GROUP_REPORT) { //For Custom Reports
                getCustomGroupMonthlyOrWeeklyQueries(mthNo, grpIds, reportType);
            } else if (reportType == AutoReportConstants.GROUP_TREND_REPORT) {
                getTrendGrpMonthlyOrWeeklyQueries(mthNo, grpIds);
            } else if (reportType == CommonConstants.CAMPAIGN) {
                getCampPerMonthlyOrWeeklyQueries(i, grpIds);
            } else if (reportType == CommonConstants.ADGROUP) {
                getAdgrpPerMonthlyOrWeeklyQueries(i, grpIds);
            } else if (reportType == AutoReportConstants.CONVERTING_KEYWORD) {
                getConvPerformanceWeeklyQueries(i);
            } else if (reportType == CommonConstants.FACEBOOK) {
                getFacebookWeeklyOrMonthlyQueries(i, grpIds, "monthly");
            } else if (reportType == AutoReportConstants.AMAZON_STANDARD_REPORT) {
                getAmazonWeeklyOrMonthlyQueries(i, grpIds);
            }
        }
    }

    public String getWeeklyTrendGroupReport() {
        tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
        try {
            String grpIds = "503";
            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.TREND_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            getWeeklyQueries(tmpTablename, grpIds, AutoReportConstants.GROUP_TREND_REPORT, false);
            getTrendGrpMonthlyOrWeeklyMergeQueries(tmpTablename); // with this we are inserting metrics into temp table  by using getWeeklyQureis method queries.

            String basicTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear =0 order by campaign_name, week_no desc";
            finalStatsQuery = "select date_range," + sqlColumnNames + ",campaign_name from " + tmpTablename + " where yearoveryear = 0 order by campaign_name,week_no desc";
            LOGGER.info("Sem Trend query>>>>>>>>>>" + finalStatsQuery);

            ReportsAutomationCustomColumns customColumnsCol = new ReportsAutomationCustomColumns();
            finalTotals = new ArrayList();
            autoReportDownloadDao.loadObjectsForTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);

            excelGenerator = setDataToExcelGenerator();
            if (yearOverYear == true) {
                excelGenerator.setPrevYear(false);
            }
            excelGenerator.setReportColNames("Date Range," + reportColNames + ",campaign_name");
            excelGenerator.setReportName(AutoReportConstants.SEM_WEEKLY_CAMPAIGN_TREND_REPORT);
            excelGenerator.setReportType(AutoReportConstants.GROUP_TREND_REPORT);
            excelGenerator.setIsGroupReport(true);
            excelGenerator.generateNewXLSXReport();

            if (yearOverYear == true) {
                excelGenerator.setGrpNamesSql("SELECT distinct(campaign_name) as campaign_name FROM " + tmpTablename + " where  campaign_name is not null");
                excelGenerator.setYearOverYear(yearOverYear);
                finalStatsQuery = "select date_range,week_no," + sqlColumnNames + ",campaign_name,yearoveryear from " + tmpTablename + "  where REPLACE(campaign_name, '''', ' ') = ";//new code
                excelGenerator.setSql(finalStatsQuery);

                excelGenerator.generateNewXLSXReport();
            }

        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
        return null;
    }

    public String getMonthlyTrendGroupReport() {

        tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
        String grpIds = "503";
        fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + templateInfo.getClientId() + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.TREND_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;

        getMonthlyQueries(tmpTablename, grpIds, AutoReportConstants.GROUP_TREND_REPORT, false);
        getTrendGrpMonthlyOrWeeklyMergeQueries(tmpTablename);

        String baiscTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear =0 order by campaign_name, week_no desc";
        finalStatsQuery = "select date_range," + sqlColumnNames + ",campaign_name from " + tmpTablename + " where yearoveryear =0 order by campaign_name,week_no desc";
        LOGGER.info("Sem Trend query>>>>>>>>>>" + finalStatsQuery);

        ReportsAutomationCustomColumns customColumnsCol = new ReportsAutomationCustomColumns();
        finalTotals = new ArrayList();
        autoReportDownloadDao.loadObjectsForTotals(baiscTotalsQuery, finalTotals, colNamesWithTotal);

        excelGenerator = setDataToExcelGenerator();
        if (yearOverYear == true) {
            excelGenerator.setPrevYear(false);
        }
        excelGenerator.setReportColNames("Date Range," + reportColNames + ",campaign_name");
        excelGenerator.setReportName(AutoReportConstants.SEM_MONTHLY_CAMPAIGN_TREND_REPORT);
        excelGenerator.setIsGroupReport(true);
        excelGenerator.setIsMonthlyReport(1);
        excelGenerator.setReportType(AutoReportConstants.GROUP_TREND_REPORT);
        excelGenerator.setIsGroupReport(true);
        excelGenerator.generateNewXLSXReport();

        if (yearOverYear == true) {
            excelGenerator.setGrpNamesSql("SELECT distinct(campaign_name) as campaign_name FROM " + tmpTablename);
            excelGenerator.setYearOverYear(true);
            finalStatsQuery = "select date_range,week_no," + sqlColumnNames + ",campaign_name,yearoveryear from " + tmpTablename + "  where REPLACE(campaign_name, '''', ' ')=";//new code
            excelGenerator.setSql(finalStatsQuery);
            LOGGER.info("Sem Month Trend query>>>>>>>>>>" + finalStatsQuery);

            excelGenerator.generateNewXLSXReport();
        }
        try {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        } catch (Exception e) {
            LOGGER.info(e);
        }
        return "";
    }

    private void getTrendGrpMonthlyOrWeeklyMergeQueries(String tmpTablename) {
        gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,campaign_name,yearoveryear) ")
                .append("select week_no,week,year,date_range,gimpressions,gclicks,gcost,gOrders,gRevenue,gavg_pos,group_name,campaign_name,yearoveryear from ( ")
                .append(gleQuery.substring(0, gleQuery.length() - 10)).append("  )gle ON DUPLICATE KEY Update gimpressions = gle.gimpressions, gclicks = gle.gclicks, gcost = gle.gcost, gOrders = gle.gOrders, gRevenue = gle.gRevenue");
        tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

        gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,campaign_name,yearoveryear) ")
                .append("select week_no,week,year,date_range,gimpressions,gclicks,gcost,gOrders,gRevenue,gavg_pos,group_name,campaign_name,yearoveryear from ( ")
                .append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10)).append("  )gle ON DUPLICATE KEY Update gimpressions = gle.gimpressions, gclicks = gle.gclicks, gcost = gle.gcost, gOrders = gle.gOrders, gRevenue = gle.gRevenue");
        tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

        msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,mOrders,mRevenue,mavg_pos,group_name,campaign_name,yearoveryear) ")
                .append("select week_no,week,year,date_range,mimpressions,mclicks,mcost,mOrders,mRevenue,mavg_pos,group_name,campaign_name,yearoveryear from ( ")
                .append(msnQuery.substring(0, msnQuery.length() - 10)).append("  )msn ON DUPLICATE KEY Update mimpressions = msn.mimpressions, mclicks = msn.mclicks, mcost = msn.mcost, mOrders = msn.mOrders, mRevenue = msn.mRevenue");
        tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

        msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,mOrders,mRevenue,mavg_pos,group_name,campaign_name,yearoveryear) ")
                .append("select week_no,week,year,date_range,mimpressions,mclicks,mcost,mOrders,mRevenue,mavg_pos,group_name,campaign_name,yearoveryear from ( ")
                .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append("  )msn ON DUPLICATE KEY Update mimpressions = msn.mimpressions, mclicks = msn.mclicks, mcost = msn.mcost, mOrders = msn.mOrders, mRevenue = msn.mRevenue");
        tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

        yahGemMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yOrders,yRevenue,yavg_pos,group_name,campaign_name,yearoveryear) ")
                .append("select week_no,week,year,date_range,yimpressions,yclicks,ycost,yOrders,yRevenue,yavg_pos,group_name,campaign_name,yearoveryear from ( ")
                .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append("  )yah ON DUPLICATE KEY Update yimpressions = yah.yimpressions, yclicks = yah.yclicks, ycost = yah.ycost, yOrders = yah.yOrders, yRevenue = yah.yRevenue");
        tabledatainserted = autoReportDownloadDao.executeQuery(yahGemMergeQuery.toString());

        yahGemYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yOrders,yRevenue,yavg_pos,group_name,campaign_name,yearoveryear) ")
                .append("select week_no,week,year,date_range,yimpressions,yclicks,ycost,yOrders,yRevenue,yavg_pos,group_name,campaign_name,yearoveryear from ( ")
                .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append("  )yah ON DUPLICATE KEY Update yimpressions = yah.yimpressions, yclicks = yah.yclicks, ycost = yah.ycost, yOrders = yah.yOrders, yRevenue = yah.yRevenue");
        tabledatainserted = autoReportDownloadDao.executeQuery(yahGemYOYMergeQuery.toString());

        if (templateInfo.getGoogleAnalytics() != 0) {
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 4) && gAcc_id != 0) {
                if (templateInfo.getRevenueType() == 1) {
                    gaGleMergeQuery.append("insert into ").append(tmpTablename).append(" (week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gagleRevenue,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gagleRevenue,group_name,campaign_name,yearoveryear  from (")
                            .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON DUPLICATE KEY Update gaIMPRESSIONS = gagleStats.gaIMPRESSIONS,gaCLICKS = gagleStats.gaCLICKS ,gaCOST = gagleStats.gaCOST,gagleOrders= gagleStats.gagleOrders, gagleRevenue= gagleStats.gagleRevenue");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaGleMergeQuery.toString());

                    gaGleYOYMergeQuery.append("insert into ").append(tmpTablename).append(" ( week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gagleRevenue,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gagleRevenue,group_name,campaign_name,yearoveryear  from (")
                            .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON DUPLICATE KEY Update gaIMPRESSIONS = gagleStats.gaIMPRESSIONS,gaCLICKS = gagleStats.gaCLICKS ,gaCOST = gagleStats.gaCOST,gagleOrders= gagleStats.gagleOrders, gagleRevenue= gagleStats.gagleRevenue");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaGleYOYMergeQuery.toString());

                } else if (templateInfo.getRevenueType() == 2) {
                    gaGleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gaglePRevenue,gagletax,gagleshipping,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gaglePRevenue,gagletax,gagleshipping,group_name,campaign_name,yearoveryear  from (")
                            .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON DUPLICATE KEY Update gaIMPRESSIONS = gagleStats.gaIMPRESSIONS,gaCLICKS = gagleStats.gaCLICKS ,gaCOST = gagleStats.gaCOST,gagleOrders= gagleStats.gagleOrders, gaglePRevenue = gagleStats.gaglePRevenue, gagletax = gagleStats.gagletax, gagleshipping = gagleStats.gagleshipping");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaGleMergeQuery.toString());

                    gaGleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gaglePRevenue,gagletax,gagleshipping,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gagleOrders,gaglePRevenue,gagletax,gagleshipping,group_name,campaign_name,yearoveryear  from (")
                            .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON DUPLICATE KEY Update gaIMPRESSIONS = gagleStats.gaIMPRESSIONS,gaCLICKS = gagleStats.gaCLICKS ,gaCOST = gagleStats.gaCOST,gagleOrders= gagleStats.gagleOrders, gaglePRevenue = gagleStats.gaglePRevenue, gagletax = gagleStats.gagletax, gagleshipping = gagleStats.gagleshipping");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaGleYOYMergeQuery.toString());
                }

            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 6 || templateInfo.getGoogleAnalytics() == 5) && mAcc_id != 0) {
                if (templateInfo.getRevenueType() == 1) {
                    gaMsnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnRevenue,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnRevenue,group_name,campaign_name,yearoveryear  from (")
                            .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats ON DUPLICATE KEY Update gaIMPRESSIONS = gamsnStats.gaIMPRESSIONS,gaCLICKS = gamsnStats.gaCLICKS ,gaCOST = gamsnStats.gaCOST,gamsnOrders= gamsnStats.gamsnOrders, gamsnRevenue= gamsnStats.gamsnRevenue");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaMsnMergeQuery.toString());

                    gaMsnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnRevenue,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnRevenue,group_name,campaign_name,yearoveryear  from (")
                            .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats ON DUPLICATE KEY Update gaIMPRESSIONS = gamsnStats.gaIMPRESSIONS,gaCLICKS = gamsnStats.gaCLICKS ,gaCOST = gamsnStats.gaCOST,gamsnOrders= gamsnStats.gamsnOrders, gamsnRevenue= gamsnStats.gamsnRevenue");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaMsnYOYMergeQuery.toString());

                } else if (templateInfo.getRevenueType() == 2) {
                    gaMsnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnPRevenue,gamsntax,gamsnshipping,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnPRevenue,gamsntax,gamsnshipping,group_name,campaign_name,yearoveryear  from (")
                            .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats ON DUPLICATE KEY Update gaIMPRESSIONS = gamsnStats.gaIMPRESSIONS,gaCLICKS = gamsnStats.gaCLICKS ,gaCOST = gamsnStats.gaCOST,gamsnOrders= gamsnStats.gamsnOrders, gamsnPRevenue= gamsnStats.gamsnPRevenue,gamsntax=gamsnStats.gamsntax,gamsnshipping=gamsnStats.gamsnshipping");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaMsnMergeQuery.toString());

                    gaMsnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnPRevenue,gamsntax,gamsnshipping,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gamsnOrders,gamsnPRevenue,gamsntax,gamsnshipping,group_name,campaign_name,yearoveryear  from (")
                            .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats ON DUPLICATE KEY Update gaIMPRESSIONS = gamsnStats.gaIMPRESSIONS,gaCLICKS = gamsnStats.gaCLICKS ,gaCOST = gamsnStats.gaCOST,gamsnOrders= gamsnStats.gamsnOrders, gamsnPRevenue= gamsnStats.gamsnPRevenue,gamsntax=gamsnStats.gamsntax,gamsnshipping=gamsnStats.gamsnshipping");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaMsnYOYMergeQuery.toString());

                }
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                if (templateInfo.getRevenueType() == 1) {
                    gaYahGemMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahRevenue,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahRevenue,group_name,campaign_name,yearoveryear  from (")
                            .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats ON DUPLICATE KEY Update gaIMPRESSIONS = gayahStats.gaIMPRESSIONS,gaCLICKS = gayahStats.gaCLICKS ,gaCOST = gayahStats.gaCOST,gayahorders= gayahStats.gayahorders,gayahRevenue= gayahStats.gayahRevenue");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaYahGemMergeQuery.toString());

                    gaYahGemYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahRevenue,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahRevenue,group_name,campaign_name,yearoveryear  from (")
                            .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats ON DUPLICATE KEY Update gaIMPRESSIONS = gayahStats.gaIMPRESSIONS,gaCLICKS = gayahStats.gaCLICKS ,gaCOST = gayahStats.gaCOST,gayahorders= gayahStats.gayahorders,gayahRevenue= gayahStats.gayahRevenue");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaYahGemYOYMergeQuery.toString());

                } else if (templateInfo.getRevenueType() == 2) {
                    gaYahGemMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahPRevenue,gayahtax,gayahshipping,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahPRevenue,gayahtax,gayahshipping,group_name,campaign_name,yearoveryear  from (")
                            .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats ON DUPLICATE KEY Update gaIMPRESSIONS = gayahStats.gaIMPRESSIONS,gaCLICKS = gayahStats.gaCLICKS ,gaCOST = gayahStats.gaCOST,gayahorders= gayahStats.gayahorders,gayahPRevenue= gayahStats.gayahPRevenue,gayahtax = gayahStats.gayahtax,gayahshipping = gayahStats.gayahshipping");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaYahGemMergeQuery.toString());

                    gaYahGemYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahPRevenue,gayahtax,gayahshipping,group_name,campaign_name,yearoveryear )")
                            .append("select week_no,week,year,date_range,gaIMPRESSIONS,gaCLICKS,gaCOST,gayahorders,gayahPRevenue,gayahtax,gayahshipping,group_name,campaign_name,yearoveryear  from (")
                            .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats ON DUPLICATE KEY Update gaIMPRESSIONS = gayahStats.gaIMPRESSIONS,gaCLICKS = gayahStats.gaCLICKS ,gaCOST = gayahStats.gaCOST,gayahorders= gayahStats.gayahorders,gayahPRevenue= gayahStats.gayahPRevenue,gayahtax = gayahStats.gayahtax,gayahshipping = gayahStats.gayahshipping");
                    tabledatainserted = autoReportDownloadDao.executeQuery(gaYahGemYOYMergeQuery.toString());

                }
            }
        }
    }

    private void getTrendGrpMonthlyOrWeeklyQueries(int i, String grpIds) {
        gleQuery.append("select  ").append(i).append(" as week_no,")
                .append("  Week,year, Date_Range,sum(impressions) as gimpressions,sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as  gRevenue,CASE sum(impressions) WHEN 0 THEN 0 ELSE ( Sum(avg_pos) / Sum(impressions) ) END AS gavg_pos,")
                .append(" group_name,replace(campaign_name,' ','_') as campaign_name,0 as yearoveryear,campaign_id,account_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos,group_name,Week,year,Date_Range,campaign_name,campaign_id,b1.account_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos, client_id , ")
                .append(" c.campaign_name, c.campaign_id as id , account_id   from ").append(gleseStatsTableName).append(" s, (select distinct(campaign_id),campaign_name from campaign_structure ) c  where s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append("   account_id =").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) b1 ")
                .append(" join ").append(" (select * from ( ").append(" (select * from reports_automation_groups where  account_id =")
                .append(gAcc_id).append(" and component_level = 1) grps ").append(" join ")
                .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(" )) gmstr ").append(" on gmstr.campaign_id = b1.id ")
                .append(" UNION ALL SELECT 0 AS IMPRESSIONS,0 AS CLICKS ,0 AS COST ,0 AS orders,0 AS TOTAL_CONV_VALUE,0 AS AVG_POS,'WoW Campaign Report' AS GROUP_NAME,")
                .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year,'").append(dateRange).append("' as Date_Range,a.CAMPAIGN_NAME,a.campaign_id ,").append(gAcc_id).append(" as account_id  from ( ")
                .append("SELECT campaign_name,campaign_id FROM campaign_structure WHERE campaign_id IN")
                .append("(SELECT CAMPAIGN_ID FROM reports_automation_groups WHERE  account_id =").append(gAcc_id).append(" AND COMPONENT_LEVEL = 1 AND GROUP_ID IN (").append(grpIds).append(")))a")
                .append(" ) k group by Week,year, Date_Range,group_name, campaign_id union all ");

        msnQuery.append("select  ").append(i).append(" as week_no,")
                .append("  Week,year, Date_Range,sum(impressions) as mimpressions,sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as  mRevenue,CASE sum(impressions) WHEN 0 THEN 0 ELSE ( Sum(avg_pos) / Sum(impressions) ) END AS mavg_pos,")
                .append(" group_name,replace(campaign_name,' ','_') as campaign_name,0 as yearoveryear,campaign_id,account_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos,group_name,Week,year,Date_Range,campaign_name,campaign_id,b1.account_id from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos, client_id , ")
                .append(" c.campaign_name, c.campaign_id as id , account_id  from ").append(msnseStatsTableName).append(" s, (select distinct(campaign_id),campaign_name from bing_campaign_structure ) c  where s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append("   account_id =").append(mAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) b1 ")
                .append(" join ").append(" (select * from ( ").append(" (select * from reports_automation_groups where  account_id =")
                .append(mAcc_id).append(" and component_level = 1) grps ").append(" join ")
                .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(" )) gmstr ").append(" on gmstr.campaign_id = b1.id ")
                .append(" UNION ALL SELECT 0 AS IMPRESSIONS,0 AS CLICKS ,0 AS COST ,0 AS orders,0 AS TOTAL_CONV_VALUE,0 AS AVG_POS,'WoW Campaign Report' AS GROUP_NAME,")
                .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year,'").append(dateRange).append("' as Date_Range,a.CAMPAIGN_NAME,a.campaign_id ,").append(mAcc_id).append(" as account_id  from ( ")
                .append("SELECT campaign_name,campaign_id FROM bing_campaign_structure WHERE campaign_id IN")
                .append("(SELECT CAMPAIGN_ID FROM reports_automation_groups WHERE  account_id =").append(mAcc_id).append(" AND COMPONENT_LEVEL = 1 AND GROUP_ID IN (").append(grpIds).append(")))a")
                .append(" ) k group by Week,year, Date_Range,group_name, campaign_id union all ");

        yahGemQuery.append("select  ").append(i).append(" as week_no,")
                .append("  Week,year, Date_Range,sum(impressions) as yimpressions,sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as  yRevenue,CASE sum(impressions) WHEN 0 THEN 0 ELSE ( Sum(avg_pos) / Sum(impressions) ) END AS yavg_pos,")
                .append(" group_name,replace(campaign_name,' ','_') as campaign_name,0 as yearoveryear,campaign_id,account_id  from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos,group_name,Week,year,Date_Range,campaign_name,campaign_id,b1.account_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos, client_id , ")
                .append(" c.campaign_name, c.campaign_id as id , account_id   from ").append(yahseStatsTableName).append(" s, (select distinct(campaign_id),campaign_name from yahgemini_campaign_structure ) c  where s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append("   account_id =").append(yahAcc_id).append(" and ").append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append("  ) b1 ")
                .append(" join ").append(" (select * from ( ").append(" (select * from reports_automation_groups where  account_id =")
                .append(yahAcc_id).append(" and component_level = 1) grps ").append(" join ")
                .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(" )) gmstr ").append(" on gmstr.campaign_id = b1.id ")
                .append(" UNION ALL SELECT 0 AS IMPRESSIONS,0 AS CLICKS ,0 AS COST ,0 AS orders,0 AS TOTAL_CONV_VALUE,0 AS AVG_POS,'WoW Campaign Report' AS GROUP_NAME,")
                .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year,'").append(dateRange).append("' as Date_Range,a.CAMPAIGN_NAME,a.campaign_id ,").append(yahAcc_id).append(" as account_id  from ( ")
                .append("SELECT campaign_name,campaign_id FROM yahgemini_campaign_structure WHERE campaign_id IN")
                .append("(SELECT CAMPAIGN_ID FROM reports_automation_groups WHERE  account_id =").append(yahAcc_id).append(" AND COMPONENT_LEVEL = 1 AND GROUP_ID IN (").append(grpIds).append(")))a")
                .append(" ) k group by Week,year, Date_Range,group_name, campaign_id union all ");

        gleYOYQuery.append("select  ").append(i).append(" as week_no,")
                .append("  Week,year, Date_Range,sum(impressions) as gimpressions,sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as  gRevenue,CASE sum(impressions) WHEN 0 THEN 0 ELSE ( Sum(avg_pos) / Sum(impressions) ) END AS gavg_pos,")
                .append(" group_name,replace(campaign_name,' ','_') as campaign_name,1 as yearoveryear,campaign_id,account_id  from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos,group_name,Week,year,Date_Range,campaign_name,campaign_id,b1.account_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos, client_id , ")
                .append(" c.campaign_name, c.campaign_id as id ,account_id   from ").append(gleseStatsTableName).append(" s, (select distinct(campaign_id),campaign_name from campaign_structure ) c  where s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append("   account_id =").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append("  ) b1 ")
                .append(" join ").append(" (select * from ( ").append(" (select * from reports_automation_groups where  account_id =")
                .append(gAcc_id).append(" and component_level = 1) grps ").append(" join ")
                .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(" )) gmstr ").append(" on gmstr.campaign_id = b1.id ")
                .append(" UNION ALL SELECT 0 AS IMPRESSIONS,0 AS CLICKS ,0 AS COST ,0 AS orders,0 AS TOTAL_CONV_VALUE,0 AS AVG_POS,'WoW Campaign Report' AS GROUP_NAME,")
                .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year,'").append(yoyDateRange).append("' as Date_Range,a.CAMPAIGN_NAME,a.campaign_id ,").append(gAcc_id).append(" as account_id  from ( ")
                .append("SELECT campaign_name,campaign_id FROM campaign_structure WHERE campaign_id IN")
                .append("(SELECT CAMPAIGN_ID FROM reports_automation_groups WHERE  account_id =").append(gAcc_id).append(" AND COMPONENT_LEVEL = 1 AND GROUP_ID IN (").append(grpIds).append(")))a")
                .append(" ) k group by Week,year, Date_Range,group_name, campaign_id union all ");

        msnYOYQuery.append("select  ").append(i).append(" as week_no,")
                .append("  Week,year, Date_Range,sum(impressions) as mimpressions,sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as  mRevenue,CASE sum(impressions) WHEN 0 THEN 0 ELSE ( Sum(avg_pos) / Sum(impressions) ) END AS mavg_pos,")
                .append(" group_name,replace(campaign_name,' ','_') as campaign_name,1 as yearoveryear,campaign_id,account_id from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos,group_name,Week,year,Date_Range,campaign_name,campaign_id ,b1.account_id from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos, client_id , ")
                .append(" c.campaign_name, c.campaign_id as id ,account_id  from ").append(msnseStatsTableName).append(" s, (select distinct(campaign_id),campaign_name from bing_campaign_structure ) c  where s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append("   account_id =").append(mAcc_id).append(" and ").append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append("  ) b1 ")
                .append(" join ").append(" (select * from ( ").append(" (select * from reports_automation_groups where  account_id =")
                .append(mAcc_id).append(" and component_level = 1) grps ").append(" join ")
                .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(" )) gmstr ").append(" on gmstr.campaign_id = b1.id ")
                .append(" UNION ALL SELECT 0 AS IMPRESSIONS,0 AS CLICKS ,0 AS COST ,0 AS orders,0 AS TOTAL_CONV_VALUE,0 AS AVG_POS,'WoW Campaign Report' AS GROUP_NAME,")
                .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year,'").append(yoyDateRange).append("' as Date_Range,a.CAMPAIGN_NAME,a.campaign_id ,").append(mAcc_id).append(" as account_id  from ( ")
                .append("SELECT campaign_name,campaign_id FROM bing_campaign_structure WHERE campaign_id IN")
                .append("(SELECT CAMPAIGN_ID FROM reports_automation_groups WHERE  account_id =").append(mAcc_id).append(" AND COMPONENT_LEVEL = 1 AND GROUP_ID IN (").append(grpIds).append(")))a")
                .append(" ) k group by Week,year, Date_Range,group_name, campaign_id union all ");

        yahGemYOYQuery.append("select  ").append(i).append(" as week_no,")
                .append("  Week,year, Date_Range,sum(impressions) as yimpressions,sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as  yRevenue,CASE sum(impressions) WHEN 0 THEN 0 ELSE ( Sum(avg_pos) / Sum(impressions) ) END AS yavg_pos,")
                .append(" group_name,replace(campaign_name,' ','_') as campaign_name,1 as yearoveryear,campaign_id,account_id  from ( select impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,avg_pos,group_name,Week,year,Date_Range,campaign_name,campaign_id,b1.account_id  from ")
                .append(" (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost , ").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos, client_id , ")
                .append(" c.campaign_name, c.campaign_id as id ,account_id   from ").append(yahseStatsTableName).append(" s, (select distinct(campaign_id),campaign_name from yahgemini_campaign_structure ) c  where s.campaign_id = c.campaign_id and ").append(isMonthTillDate)
                .append("   account_id =").append(yahAcc_id).append(" and ").append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append("  ) b1 ")
                .append(" join ").append(" (select * from ( ").append(" (select * from reports_automation_groups where  account_id =")
                .append(yahAcc_id).append(" and component_level = 1) grps ").append(" join ")
                .append(" (select group_id as id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(" )) gmstr ").append(" on gmstr.campaign_id = b1.id ")
                .append(" UNION ALL SELECT 0 AS IMPRESSIONS,0 AS CLICKS ,0 AS COST ,0 AS orders,0 AS TOTAL_CONV_VALUE,0 AS AVG_POS,'WoW Campaign Report' AS GROUP_NAME,")
                .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year,'").append(yoyDateRange).append("' as Date_Range,a.CAMPAIGN_NAME,a.campaign_id ,").append(yahAcc_id).append(" as account_id  from ( ")
                .append("SELECT campaign_name,campaign_id FROM yahgemini_campaign_structure WHERE campaign_id IN")
                .append("(SELECT CAMPAIGN_ID FROM reports_automation_groups WHERE  account_id =").append(yahAcc_id).append(" AND COMPONENT_LEVEL = 1 AND GROUP_ID IN (").append(grpIds).append(")))a")
                .append(" ) k group by Week,year, Date_Range,group_name, campaign_id union all ");

        if (templateInfo.getGoogleAnalytics() != 0) {

            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && gAcc_id != 0) {

                gaGleQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name,campaign_name,0 as yearoveryear,se_campaign_id ,account_id   ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(glegaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(gAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from campaign_structure where  account_id in (").append(seAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" )  b group by   week,year, Date_Range,group_name, campaign_name union all ");

                gaGleYOYQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name,campaign_name,1 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(yoyDateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(glegaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(gAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from campaign_structure where  account_id in (").append(seAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" )  b group by   week,year, Date_Range,group_name, campaign_name union all ");

            } else if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && gAcc_id != 0) {
                gaGleQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders, sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax, sum(GA_SHIPPING) as gagleShipping,group_name,campaign_name,0 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id   from ").append(glegaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(gAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from campaign_structure where  account_id in (").append(seAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" ) g group by   week,year, Date_Range,group_name, campaign_name union all ");
                gaGleYOYQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders, sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax, sum(GA_SHIPPING) as gagleShipping,group_name,campaign_name,1 as yearoveryear,se_campaign_id,account_id   ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(yoyDateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(glegaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(gAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(gAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from campaign_structure where  account_id in (").append(seAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" ) g group by   week,year, Date_Range,group_name, campaign_name union all ");

            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 1 && mAcc_id != 0) {
                gaMsnQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnorders,sum(GA_REVENUE) as gamsnRevenue,group_name,campaign_name,0 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(msngaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(mAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(mAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from bing_campaign_structure where  se_account_id in (").append(msnseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" )  b group by   week,year, Date_Range,group_name, campaign_name union all ");

                gaMsnYOYQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name,campaign_name,1 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(yoyDateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(msngaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(mAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(mAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from bing_campaign_structure where  se_account_id in (").append(msnseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" )  b group by   week,year, Date_Range,group_name, campaign_name union all ");

            } else if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && templateInfo.getRevenueType() == 2 && mAcc_id != 0) {
                gaMsnQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders, sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax, sum(GA_SHIPPING) as gamsnShipping,group_name,campaign_name,0 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id   from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id   from ").append(msngaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(mAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(mAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from bing_campaign_structure where  se_account_id in (").append(msnseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" ) g group by   week,year, Date_Range,group_name, campaign_name union all ");

                gaMsnYOYQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders, sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax, sum(GA_SHIPPING) as gamsnShipping,group_name,campaign_name,1 as yearoveryear,se_campaign_id,account_id   ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(yoyDateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(msngaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(mAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(mAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from bing_campaign_structure where  se_account_id in (").append(msnseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" ) g group by   week,year, Date_Range,group_name, campaign_name union all ");

            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 1 && yahAcc_id != 0) {
                gaYahGemQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders,sum(GA_REVENUE) as gayahRevenue,group_name,campaign_name,0 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(yahgaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(yahAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(yahAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from yahgemini_campaign_structure where  se_account_id in (").append(yahseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" )  b group by   week,year, Date_Range,group_name, campaign_name union all ");

                gaYahGemYOYQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders,sum(GA_REVENUE) as gayahRevenue,group_name,campaign_name,1 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_REVENUE,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(yoyDateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(yahgaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(yahAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(yahAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from yahgemini_campaign_structure where  se_account_id in (").append(yahseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" )  b group by   week,year, Date_Range,group_name, campaign_name union all ");

            } else if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && templateInfo.getRevenueType() == 2 && yahAcc_id != 0) {
                gaYahGemQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders, sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax, sum(GA_SHIPPING) as gayahShipping,group_name,campaign_name,0 as yearoveryear,se_campaign_id,account_id    ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id   from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(dateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id   from ").append(yahgaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(yahAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo).append(" and year = ").append(year).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(yahAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from yahgemini_campaign_structure where  se_account_id in (").append(yahseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" ) g group by   week,year, Date_Range,group_name, campaign_name union all ");

                gaYahGemYOYQuery.append("  select ").append(i).append(" as week_no,")
                        .append("  Week,year,  Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahorders, sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax, sum(GA_SHIPPING) as gayahShipping,group_name,campaign_name,1 as yearoveryear,se_campaign_id,account_id   ")
                        .append(" from (      ").append(" select Week,year,  Date_Range, -1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,GA_TRANSACTIONS,GA_PRODUCT_REVENUE,GA_TAX,GA_SHIPPING,group_name,b1.campaign_name,b1.se_campaign_id,b1.account_id  from (  ")
                        .append(" (select  ").append("'RW").append(i).append(" ' as Week,year, '").append(yoyDateRange)
                        .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost,sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , SUM(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE,sum(GA_TAX) as GA_TAX,sum(GA_SHIPPING) as GA_SHIPPING, ")
                        .append(" account_id,replace(campaign_name,' ','_') as campaign_name,se_campaign_id  from ").append(yahgaStatsTableName).append(" where ").append(isMonthTillDate)
                        .append("   client_id   = ").append(templateInfo.getClientId()).append(" and  account_id in (").append(yahAcc_id).append(") and  ")
                        .append(durCondition).append("=").append(durNo + 1).append(" and year = ").append(year - 1).append(" group by campaign_name,year, account_id ) b1  ")
                        .append(" join  ").append(" (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from  ( (select * from reports_automation_groups where client_id  = ")
                        .append(templateInfo.getClientId()).append(" and  account_id in ( ").append(yahAcc_id).append(") and ").append(" component_level = 1) grps inner join  (   ")
                        .append(" select * from yahgemini_campaign_structure where  se_account_id in (").append(yahseAccId).append(" )      ")
                        .append(" ) cmp on grps.campaign_id = cmp.campaign_id  )) a1 ").append(" inner join  (select group_id id,group_name from lxr_kpi_group_master where group_id in (")
                        .append(grpIds).append(") ) a2 on(a1.group_id = a2.id) )   ")
                        .append(" grpnames on(grpnames.account_id = b1.account_id and grpnames.campaign_name = b1.campaign_name) )   ")
                        .append(" ) g group by   week,year, Date_Range,group_name, campaign_name union all ");

            }
        }
    }

    public void getMonthlyQueries(String tableName, String grpIds, int reportType) {
        seAccId = accountDetailsDao.getAccountSeId(gAcc_id);
        if (mAcc_id != 0) {
            msnseAccId = accountDetailsDao.getAccountSeId(mAcc_id);
        }
        if (yahAcc_id != 0) {
            yahseAccId = accountDetailsDao.getAccountSeId(yahAcc_id);
        }
        gleseStatsTableName = "ne_autoreportsmonthlysestats";
        glegaStatsTableName = "ne_autoreportsmonthlygastats";
        msnseStatsTableName = "ne_autoreportsmonthlybingsestat";
        msngaStatsTableName = "ne_autoreportsmonthlybinggastats";
        yahseStatsTableName = "ne_autoreportsyahmonthlysestats";
        yahgaStatsTableName = "ne_autoreportsyahmonthlygastats";
        durCondition = "month_no";
        isMonthTillDate = " IS_MONTH_TILL_DATE = 0 and ";
        getTmpGroupTableSql(tableName);
        gleQuery = new StringBuilder();
        gaGleQuery = new StringBuilder();
        gleYOYQuery = new StringBuilder();
        gaGleYOYQuery = new StringBuilder();
        gleMergeQuery = new StringBuilder();
        gaGleMergeQuery = new StringBuilder();

        msnQuery = new StringBuilder();
        gaMsnQuery = new StringBuilder();
        msnYOYQuery = new StringBuilder();
        gaMsnYOYQuery = new StringBuilder();
        msnMergeQuery = new StringBuilder();
        gaMsnMergeQuery = new StringBuilder();
        msnYOYMergeQuery = new StringBuilder();
        gaMsnYOYMergeQuery = new StringBuilder();

        yahGemQuery = new StringBuilder();
        gaYahGemQuery = new StringBuilder();
        yahGemYOYQuery = new StringBuilder();
        gaYahGemYOYQuery = new StringBuilder();
        yahGemMergeQuery = new StringBuilder();
        gaYahGemMergeQuery = new StringBuilder();
        yahGemYOYMergeQuery = new StringBuilder();
        gaYahGemYOYMergeQuery = new StringBuilder();

        cal = Calendar.getInstance();
        fcalYOY = Calendar.getInstance();
        fcalYOY.add(Calendar.MONTH, -1);
        cal.add(Calendar.MONTH, -1);
        int loopCnt = 0;
        if (reportType == CommonConstants.CAMPAIGN || reportType == CommonConstants.ADGROUP || reportType == AutoReportConstants.CONVERTING_KEYWORD) {
            loopCnt = 2;
        } else {
            loopCnt = monthlyLoopCnt;
        }

        for (int i = 1; i < loopCnt; i++) {
            if (i != 1) {
                cal.add(Calendar.MONTH, -1);
            }
            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
            // fdate = cal.get(CaleCommonConstants.ndar.DATE);
            cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            fdate = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            fyear = cal.get(Calendar.YEAR);
            fmonthNo = cal.get(Calendar.MONTH);
            fmonthName = CommonFunctions.getMonthName(fmonthNo);
//            fromDate = new DateConverter(cal).getOracleDateForm();
            fromDate = (simpleDateFormat.format(cal.getTime()));

            // fcalYOY.set(Calendar.WEEK_OF_YEAR, weekOfYear);
            fcalYOY.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            fcalYOY.set(Calendar.YEAR, fyear - 1);
            fcalYOY.set(Calendar.MONTH, fmonthNo);

            yoyfdate = fcalYOY.get(Calendar.DATE);
            yoyfyear = fcalYOY.get(Calendar.YEAR);
            yoyfmonthNo = fcalYOY.get(Calendar.MONTH);
            yoyfmonthName = CommonFunctions.getMonthName(yoyfmonthNo);

            // fromDate = new DateConverter(cal).getOracleDateForm();
            yoyFromDate = new DateConverter(fcalYOY).getOracleDateForm();

            // cal.add(Calendar.MONTH, -1);
            //tdate = cal.get(Calendar.DATE);
            tdate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            fcalYOY.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            tyear = cal.get(Calendar.YEAR);
            tmonthNo = cal.get(Calendar.MONTH);
            tmonthName = CommonFunctions.getMonthName(tmonthNo);
//            toDate = new DateConverter(cal).getOracleDateForm();
            toDate = (simpleDateFormat.format(cal.getTime()));
            yoytdate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            yoytyear = fcalYOY.get(Calendar.YEAR);
            yoytmonthNo = fcalYOY.get(Calendar.MONTH);
            yoytmonthName = CommonFunctions.getMonthName(yoytmonthNo);

            dateRange = fmonthName + " " + fdate + "," + " " + fyear + "-" + tmonthName + " " + tdate + "," + " " + tyear;
            yoyDateRange = yoyfmonthName + " " + yoyfdate + "," + " " + yoyfyear + "-" + yoytmonthName + " " + yoytdate + "," + " " + yoytyear;
            durNo = fmonthNo;
            year = fyear;
            if (i == 1) {
                reportingDate = dateRange;
            }
            if (reportType == AutoReportConstants.GROUP_REPORT || reportType == AutoReportConstants.STANDARD_REPORT) {
                getGroupMonthlyOrWeeklyQueries(i, grpIds, reportType);
            } else if (reportType == AutoReportConstants.GROUP_TREND_REPORT) {
            } else if (reportType == CommonConstants.CAMPAIGN) {
                getCampPerMonthlyOrWeeklyQueries(i, grpIds);
            } else if (reportType == CommonConstants.ADGROUP) {
                getAdgrpPerMonthlyOrWeeklyQueries(i, grpIds);
            } else if (reportType == AutoReportConstants.CONVERTING_KEYWORD) {
                getConvPerformanceWeeklyQueries(i);
            }
        }
    }

    public String getMonthlyGroupReport(String grpIds) {
        String finalGroupIds = "";
        try {
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();

            if (grpIds.equalsIgnoreCase("-1")) {
                finalGroupIds = "501,502";
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.BRAND_AND_NONBRAND_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
                isCustom = false;
            } else {
                finalGroupIds = grpIds;
                fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.GROUP_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
                isCustom = true;
            }
            getMonthlyQueries(tmpTablename, finalGroupIds, AutoReportConstants.GROUP_REPORT, false);
//            getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);
            if (grpIds.equalsIgnoreCase("-1")) {
                getBrandNonBrandMonthlyOrWeeklyMergeQueries(tmpTablename);
            } else {
//                getGroupMonthlyOrWeeklyMergeQueries(tmpTablename);
                getCustomGroupMonthlyOrWeeklyMergeQueries(tmpTablename);
            }

            String basicTotalsQuery = "select " + basicTotalSqlColumnNames + ",group_name,week_no  from " + tmpTablename + " where yearoveryear = 0 order by group_name, week_no desc";
            finalStatsQuery = "select date_range," + sqlColumnNames + ",group_name from " + tmpTablename + " where yearoveryear = 0 order by group_name,week_no desc";

            LOGGER.info("Sem Group query>>>>>>>>>>" + finalStatsQuery);
            finalTotals = new ArrayList();
            finalTotals = autoReportDownloadDao.loadObjectsForTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);

            excelGenerator = setDataToExcelGenerator();
            if (yearOverYear == true) {
                excelGenerator.setPrevYear(false);
            }
            excelGenerator.setReportColNames("Date Range," + reportColNames + ",group_name");
            excelGenerator.setIsGroupReport(true);
            excelGenerator.setIsMonthlyReport(1);
            if (finalGroupIds.equalsIgnoreCase("501,502")) {
                excelGenerator.setLoopCnt(monthlyLoopCnt - 2);
                excelGenerator.setReportName(AutoReportConstants.SEM_MONTHLY_BRAND_AND_NON_BRAND_REPORT);
                excelGenerator.setReportType(AutoReportConstants.BRAND_AND_NONBRAND_REPORT);
                excelGenerator.setGroup_name("Brand");
                excelGenerator.setGroup_name1("Non Brand");
            } else {
                excelGenerator.setReportName(AutoReportConstants.SEM_MONTHLY_CATEGORY_REPORT);
                excelGenerator.setReportType(AutoReportConstants.GROUP_REPORT);
            }
            excelGenerator.generateNewXLSXReport();

            //for year over report
            if (yearOverYear == true) {
                excelGenerator.setGrpNamesSql("SELECT GROUP_NAME FROM lxr_kpi_group_master WHERE GROUP_ID IN(" + finalGroupIds + ") ORDER BY GROUP_ID");
                finalStatsQuery = "select date_range,week_no," + sqlColumnNames + ",group_name,yearoveryear from " + tmpTablename + "  where group_name=";//new code
                excelGenerator.setSql(finalStatsQuery);
                LOGGER.info("Sem Group query>>>>>>>>>>" + finalStatsQuery);
                excelGenerator.setYearOverYear(true);
                excelGenerator.generateNewXLSXReport();
            }
        } catch (Exception ex) {
            LOGGER.info(ex);
        } finally {
            LOGGER.info("drop tmp table " + tmpTablename);
            autoReportDownloadDao.dropTable(tmpTablename);
        }
        return "";
    }

    private void getGroupMonthlyOrWeeklyMergeQueries(String tmpTablename) {
        //New Conditions
        if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id == 0) {// Only GLE
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());
        } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id == 0) {// Only MSN

            msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnQuery.substring(0, msnQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

        } else if (gAcc_id == 0 && mAcc_id == 0 && yahAcc_id != 0) {// Only YG

            yahGemMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,yavg_pos,group_name,yearoveryear) ").append(yahGemQuery.substring(0, yahGemQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,yavg_pos,group_name,yearoveryear) ").append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id == 0) {// Gle & MSN
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            msnMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnQuery.substring(0, msnQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos");
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos ");
            autoReportDownloadDao.updateQuery(msnYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id != 0) {// GLE & YG         
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id != 0) {// MSN & YG            
            msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnQuery.substring(0, msnQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id != 0) {// GLE + MSN + YG 
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ")
                    .append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ")
                    .append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            msnMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnQuery.substring(0, msnQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos");
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos ");
            autoReportDownloadDao.updateQuery(msnYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());
        }
        // GA Merge Queries
        if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 1) {  // GA Total Revenue
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {
                gaGleMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost, tmp.gagleOrders= gagleStats.gagleOrders,tmp.gagleRevenue= gagleStats.gagleRevenue");

                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());

                gaGleYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and  tmp.yearoveryear = gagleStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost,"
                        + " tmp.gagleOrders= gagleStats.gagleOrders,tmp.gagleRevenue= gagleStats.gagleRevenue");

                autoReportDownloadDao.updateQuery(gaGleYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {
                gaMsnMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnRevenue= gamsnStats.gamsnRevenue");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());

                gaMsnYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnRevenue= gamsnStats.gamsnRevenue");
                autoReportDownloadDao.updateQuery(gaMsnYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                gaYahGemMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahRevenue= gayahStats.gayahRevenue");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());

                gaYahGemYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahRevenue= gayahStats.gayahRevenue");
                autoReportDownloadDao.updateQuery(gaYahGemYOYMergeQuery.toString());
            }
        }

        if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 2) {  // GA Product Revenue
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {
                gaGleMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost, "
                        + "tmp.gagleOrders= gagleStats.gagleOrders,tmp.gaglePRevenue= gagleStats.gaglePRevenue, tmp.gagletax = gagleStats.gagletax, tmp.gagleshipping = gagleStats.gagleshipping");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());

                gaGleYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost,"
                        + " tmp.gagleOrders= gagleStats.gagleOrders,tmp.gaglePRevenue= gagleStats.gaglePRevenue, tmp.gagletax = gagleStats.gagletax, tmp.gagleshipping = gagleStats.gagleshipping");

                autoReportDownloadDao.updateQuery(gaGleYOYMergeQuery.toString());
            }
//            if (mAcc_id != 0) 
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {
                gaMsnMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnPRevenue= gamsnStats.gamsnPRevenue, tmp.gamsnTax = gamsnStats.gamsnTax, tmp.gamsnShipping = gamsnStats.gamsnShipping");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());

                gaMsnYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnPRevenue= gamsnStats.gamsnPRevenue,tmp.gamsnTax = gamsnStats.gamsnTax, tmp.gamsnShipping = gamsnStats.gamsnShipping");
                autoReportDownloadDao.updateQuery(gaMsnYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                gaYahGemMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahPRevenue= gayahStats.gayahPRevenue, tmp.gayahtax=gayahStats.gayahtax, tmp.gayahshipping= gayahStats.gayahshipping");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());

                gaYahGemYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahPRevenue= gayahStats.gayahPRevenue,tmp.gayahtax=gayahStats.gayahtax, tmp.gayahshipping= gayahStats.gayahshipping");
                autoReportDownloadDao.updateQuery(gaYahGemYOYMergeQuery.toString());
            }
        }
    }

    private void getBrandNonBrandMonthlyOrWeeklyMergeQueries(String tmpTablename) {
        //New Conditions
        if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id == 0) {// Only GLE
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());
        } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id == 0) {// Only MSN

            msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnQuery.substring(0, msnQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

        } else if (gAcc_id == 0 && mAcc_id == 0 && yahAcc_id != 0) {// Only YG

            yahGemMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,yavg_pos,group_name,yearoveryear) ").append(yahGemQuery.substring(0, yahGemQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,yavg_pos,group_name,yearoveryear) ").append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id == 0) {// Gle & MSN
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            msnMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnQuery.substring(0, msnQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos");
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos ");
            autoReportDownloadDao.updateQuery(msnYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id != 0) {// GLE & YG         
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id != 0) {// MSN & YG            
            msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnQuery.substring(0, msnQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear  and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id != 0) {// GLE + MSN + YG 
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            msnMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnQuery.substring(0, msnQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos");
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos ");
            autoReportDownloadDao.updateQuery(msnYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());
        }
        // GA Merge Queries
        if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 1) {  // GA Total Revenue
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {
                gaGleMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost, tmp.gagleOrders= gagleStats.gagleOrders,tmp.gagleRevenue= gagleStats.gagleRevenue");

                LOGGER.info("gaGleMergeQuery--" + gaGleMergeQuery);
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());

                gaGleYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and  tmp.yearoveryear = gagleStats.yearoveryear and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost,"
                        + " tmp.gagleOrders= gagleStats.gagleOrders,tmp.gagleRevenue= gagleStats.gagleRevenue");

                autoReportDownloadDao.updateQuery(gaGleYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {
                gaMsnMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnRevenue= gamsnStats.gamsnRevenue");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());

                gaMsnYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnRevenue= gamsnStats.gamsnRevenue");
                autoReportDownloadDao.updateQuery(gaMsnYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                gaYahGemMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahRevenue= gayahStats.gayahRevenue");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());

                gaYahGemYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahRevenue= gayahStats.gayahRevenue");
                autoReportDownloadDao.updateQuery(gaYahGemYOYMergeQuery.toString());
            }
        }

        if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 2) {  // GA Product Revenue
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {
                gaGleMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost, "
                        + "tmp.gagleOrders= gagleStats.gagleOrders,tmp.gaglePRevenue= gagleStats.gaglePRevenue, tmp.gagletax = gagleStats.gagletax, tmp.gagleshipping = gagleStats.gagleshipping");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());

                gaGleYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost,"
                        + " tmp.gagleOrders= gagleStats.gagleOrders,tmp.gaglePRevenue= gagleStats.gaglePRevenue, tmp.gagletax = gagleStats.gagletax, tmp.gagleshipping = gagleStats.gagleshipping");

                autoReportDownloadDao.updateQuery(gaGleYOYMergeQuery.toString());
            }
//            if (mAcc_id != 0) 
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {
                gaMsnMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnPRevenue= gamsnStats.gamsnPRevenue, tmp.gamsnTax = gamsnStats.gamsnTax, tmp.gamsnShipping = gamsnStats.gamsnShipping");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());

                gaMsnYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnPRevenue= gamsnStats.gamsnPRevenue,tmp.gamsnTax = gamsnStats.gamsnTax, tmp.gamsnShipping = gamsnStats.gamsnShipping");
                autoReportDownloadDao.updateQuery(gaMsnYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                gaYahGemMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahPRevenue= gayahStats.gayahPRevenue, tmp.gayahtax=gayahStats.gayahtax, tmp.gayahshipping= gayahStats.gayahshipping");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());

                gaYahGemYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahPRevenue= gayahStats.gayahPRevenue,tmp.gayahtax=gayahStats.gayahtax, tmp.gayahshipping= gayahStats.gayahshipping");
                autoReportDownloadDao.updateQuery(gaYahGemYOYMergeQuery.toString());
            }
        }
    }

    private void getCustomGroupMonthlyOrWeeklyQueries(int i, String grpIds, int reportType) {

        gleQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as gimpressions, sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as ")
                .append(" gRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS gavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                .append(" client_id ,adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                .append(isMonthTillDate).append("  ").append(" client_id =").append(clientId)
                .append(" and ").append("account_id = ").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo)
                .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and label_id in (").append(grpIds).append(")");
        String[] grpIdStr = grpIds.split(",");
        for (int j = 0; j < grpIdStr.length; j++) {
            gleQuery.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id,")
                    .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                    .append(" (select campaign_id from reports_automation_groups where component_level=1 and label_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(gAcc_id).append(" )");

        }

        gleQuery.append(") grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

        gleYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as gimpressions, sum(clicks) as gclicks,sum(cost) as gcost, sum(").append(orderType).append(") as gOrders,sum(total_conv_value) as ")
                .append(" gRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS gavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                .append(" client_id ,adgroup_name ,adgroup_id   from ").append(gleseStatsTableName).append(" where ")
                .append(isMonthTillDate).append("  ").append(" client_id =").append(clientId)
                .append(" and ").append("account_id = ").append(gAcc_id).append(" and ").append(durCondition).append("=").append(durNo + 1)
                .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(gAcc_id).append(" and component_level = 2 and label_id in (").append(grpIds).append(")");
        String[] grpIdStr1 = grpIds.split(",");
        for (int j = 0; j < grpIdStr1.length; j++) {
            gleYOYQuery.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id,").append(gAcc_id).append(" as account_id,")
                    .append(grpIdStr1[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                    .append(" (select campaign_id from reports_automation_groups where component_level=1 and label_id in(").append(grpIdStr1[j]).append(")").append(" and account_id= ").append(gAcc_id).append(" )");

        }

        gleYOYQuery.append(") grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

//      Bing Queries Starts here 
        msnQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as mimpressions, sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as ")
                .append(" mRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS mavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                .append(" client_id ,adgroup_name ,adgroup_id   from ").append(msnseStatsTableName).append(" where ")
                .append(isMonthTillDate).append(" level_of_detail = 2 ")
                .append(" and ").append("se_account_id = ").append(msnseAccId).append(" and ").append(durCondition).append("=").append(durNo)
                .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and label_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
        for (int j = 0; j < grpIdStr.length; j++) {
            msnQuery.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id,")
                    .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from bing_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                    .append(" (select campaign_id from reports_automation_groups where component_level=1 and label_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(mAcc_id).append(" )");
        }
        msnQuery.append(") grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

        msnYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as mimpressions, sum(clicks) as mclicks,sum(cost) as mcost, sum(").append(orderType).append(") as mOrders,sum(total_conv_value) as ")
                .append(" mRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS mavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                .append(" client_id ,adgroup_name ,adgroup_id   from ").append(msnseStatsTableName).append(" where ")
                .append(isMonthTillDate).append(" level_of_detail = 2 ")
                .append(" and ").append("se_account_id = ").append(msnseAccId).append(" and ").append(durCondition).append("=").append(durNo + 1)
                .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(mAcc_id).append(" and component_level = 2 and label_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
        for (int j = 0; j < grpIdStr.length; j++) {
            msnYOYQuery.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id,").append(mAcc_id).append(" as account_id,")
                    .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from bing_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                    .append(" (select campaign_id from reports_automation_groups where component_level=1 and label_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(mAcc_id).append(" )");

        }

        msnYOYQuery.append(") grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

//      YahooGemini Queries Starts here 
        yahGemQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as yimpressions, sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as ")
                .append(" yRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS yavg_pos,group_name , 0 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                .append(dateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                .append(" client_id ,adgroup_name ,adgroup_id   from ").append(yahseStatsTableName).append(" where ")
                .append(isMonthTillDate).append(" level_of_detail = 2 ")
                .append(" and ").append("se_account_id = ").append(yahseAccId).append(" and ").append(durCondition).append("=").append(durNo)
                .append(" and year = ").append(year).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and label_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
        for (int j = 0; j < grpIdStr.length; j++) {
            yahGemQuery.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id,")
                    .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from yahgemini_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                    .append(" (select campaign_id from reports_automation_groups where component_level=1 and label_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(yahAcc_id).append(" )");
        }
        yahGemQuery.append(") grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                .append("'RW").append(i).append(" ' as Week,").append(year).append(" as year, '")
                .append(dateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");

        yahGemYOYQuery.append("select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,sum(impressions) as yimpressions, sum(clicks) as yclicks,sum(cost) as ycost, sum(").append(orderType).append(") as yOrders,sum(total_conv_value) as ")
                .append(" yRevenue, CASE SUM(impressions) WHEN 0 THEN 0 ELSE (SUM(AVG_POS) / SUM(Impressions)) END AS yavg_pos,group_name , 1 as yearoveryear from ( select impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,avg_pos ,adgroup_name ")
                .append(" ,group_name,Week,year,Date_Range,b1.adgroup_id from(select ").append("'RW").append(i).append(" ' as Week,year,'")
                .append(yoyDateRange).append("' as Date_Range,impressions,clicks ,cost ,").append(orderType).append(",total_conv_value,(AVG_POS*Impressions) as avg_pos,")
                .append(" client_id ,adgroup_name ,adgroup_id   from ").append(yahseStatsTableName).append(" where ")
                .append(isMonthTillDate).append(" level_of_detail = 2 ")
                .append(" and ").append("se_account_id = ").append(yahseAccId).append(" and ").append(durCondition).append("=").append(durNo + 1)
                .append(" and year = ").append(year - 1).append("  ) b1 ").append(" join").append(" (   ").append(" select * from  ")
                .append(" (select client_id,account_id,group_id,component_level,adgroup_id from reports_automation_groups where  client_id =").append(clientId).append(" and ")
                .append("account_id = ").append(yahAcc_id).append(" and component_level = 2 and label_id in (").append(grpIds).append(")");
//        String[] grpIdStr = grpIds.split(",");
        for (int j = 0; j < grpIdStr.length; j++) {
            yahGemYOYQuery.append(" union ")
                    .append(" select ").append(clientId).append(" as client_id,").append(yahAcc_id).append(" as account_id,")
                    .append(grpIdStr[j]).append(" as group_id,").append(" 2 as component_level,adgroup_id from yahgemini_adgroup_structure WHERE ADGROUP_STATUS !='removed' and campaign_id in ")
                    .append(" (select campaign_id from reports_automation_groups where component_level=1 and label_id in(").append(grpIdStr[j]).append(")").append(" and account_id= ").append(yahAcc_id).append(" )");

        }

        yahGemYOYQuery.append(") grps ").append(" join ")
                .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds).append(") ) mstr ")
                .append(" on grps.group_id = mstr.id ").append(") grpnames  ")
                .append(" on(grpnames.adgroup_id = b1.adgroup_id) ")
                .append("union all (select 0 as impressions, 0 as clicks , 0 as cost ,0 as CONVERSIONS , 0 as total_conv_value, 0 as avg_pos,'xx' as adgroup_name,group_name, ")
                .append("'RW").append(i).append(" ' as Week,").append(year - 1).append(" as year, '")
                .append(yoyDateRange).append("' as Date_Range , 0 as se_adgroup_id  from lxr_kpi_group_master where group_id in (")
                .append(grpIds).append("))) k group by Week, year, Date_Range, GROUP_NAME union all ");
        if (templateInfo.getGoogleAnalytics() != 0) {

            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {

                if (templateInfo.getRevenueType() == 1) {

                    gaGleQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" ) ) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) k group by  week,year,Date_Range,group_name union all ");

                    gaGleYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_REVENUE) as gagleRevenue,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" ) ) grps inner join ")
                            .append(" ( select * from campaign_structure where  ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) )) h group by  week,year,Date_Range,group_name union all ");

                } else if (templateInfo.getRevenueType() == 2) {
                    gaGleQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue,sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" ) ) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) m group by  week,year,Date_Range,group_name union all ");

                    gaGleYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gagleOrders,sum(GA_PRODUCT_REVENUE) as gaglePRevenue, sum(GA_TAX) as gagleTax,sum(GA_SHIPPING) as gagleShipping,group_name ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign from ").append(glegaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(gAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(gAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from campaign_structure where ").append(" account_id in (").append(seAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) ))  j group by  week,year,Date_Range,group_name union all ");
                }
            }
//            Bing Analytics Queries Starts here 
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {

                if (templateInfo.getRevenueType() == 1) {

                    gaMsnQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) k group by  week,year,Date_Range,group_name union all ");

                    gaMsnYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_REVENUE) as gamsnRevenue,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" ( select * from bing_campaign_structure where  ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) )) h group by  week,year,Date_Range,group_name union all ");

                } else if (templateInfo.getRevenueType() == 2) {
                    gaMsnQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue,sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) m group by  week,year,Date_Range,group_name union all ");

                    gaMsnYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gamsnOrders,sum(GA_PRODUCT_REVENUE) as gamsnPRevenue, sum(GA_TAX) as gamsnTax,sum(GA_SHIPPING) as gamsnShipping,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign from ").append(msngaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(mAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(mAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from bing_campaign_structure where ").append(" se_account_id in (").append(msnseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) ))  j group by  week,year,Date_Range,group_name union all ");
                }
            }
//            Yahoo Gemini Analytics Queries Starts here 
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && yahAcc_id != 0) {

                if (templateInfo.getRevenueType() == 1) {

                    gaYahGemQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) k group by  week,year,Date_Range,group_name union all ");

                    gaYahGemYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_REVENUE) as gayahRevenue,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_REVENUE) as GA_REVENUE,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" ( select * from yahgemini_campaign_structure where  ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) )) h group by  week,year,Date_Range,group_name union all ");

                } else if (templateInfo.getRevenueType() == 2) {
                    gaYahGemQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue,sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,group_name,0 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(dateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS ,  sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign  from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo).append(" and year = ")
                            .append(year).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign) ))) m group by  week,year,Date_Range,group_name union all ");

                    gaYahGemYOYQuery.append(" select ").append(i).append(" as week_no,").append(" Week,year, Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as gayahOrders,sum(GA_PRODUCT_REVENUE) as gayahPRevenue, sum(GA_TAX) as gayahTax,sum(GA_SHIPPING) as gayahShipping,group_name,1 as yearoveryear ")
                            .append(" from ( select * from ( (select ").append("'RW").append(i).append(" ' as Week,year,'").append(yoyDateRange)
                            .append("' as Date_Range,-1000 as gaimpressions,-1000 as gaclicks,-1000 as gacost, sum(GA_TRANSACTIONS) as GA_TRANSACTIONS , sum(GA_PRODUCT_REVENUE) as GA_PRODUCT_REVENUE, SUM(GA_TAX) AS GA_TAX,SUM(GA_SHIPPING) AS GA_SHIPPING,")
                            .append(" account_id acc_id,replace(campaign_name,' ','_') as campaign from ").append(yahgaStatsTableName).append(" where  ")
                            .append(isMonthTillDate).append(" client_id   =").append(templateInfo.getClientId()).append(" and ")
                            .append(" account_id in (").append(yahAcc_id).append(") and  ").append(durCondition).append("=").append(durNo + 1).append(" and year = ")
                            .append(year - 1).append("  group by campaign_name,year,account_id ) b1 join (select * from(select grps.*,replace(campaign_name,' ','_') as campaign_name from ")
                            .append(" ( (select * from reports_automation_groups where client_id  =").append(templateInfo.getClientId()).append(" and ").append(" account_id in (").append(yahAcc_id).append(") and component_level = 1 and label_id in ( ").append(grpIds).append(" )) grps inner join ")
                            .append(" (select * from yahgemini_campaign_structure where ").append(" se_account_id in (").append(yahseAccId).append(")   ) adgrp on grps.campaign_id = adgrp.campaign_id  )) a1 inner join ")
                            .append(" (select group_id id ,group_name from lxr_kpi_group_master where group_id in (").append(grpIds)
                            .append(") ) a2 on(a1.label_id = a2.id) ) grpnames on( ")
                            .append(" grpnames.account_id = b1.acc_id and upper(grpnames.campaign_name) = upper(b1.campaign)) ))  j group by  week,year,Date_Range,group_name union all ");
                }
            }
        }
    }

    private void getAmazonMergeQueries(String tmpTablename) {
        try {
            if (amzAccType == 1) {
                if ("month_no".equals(durCondition)) {
                    amzMergeQuery.append("insert into ").append(tmpTablename).append("( week_no, week, YEAR, date_range, aIMPRESSIONS, aCLICKS, aCOST, aORDERS, aREVENUE, group_name,yearoveryear ) ")
                            .append("  select amazonStats.week_no,amazonStats.Week,amazonStats.year,amazonStats.Date_Range,amazonStats.aimpressions,amazonStats.aclicks, amazonStats.acost,amazonStats.aOrders,amazonStats.aRevenue,group_name,yearoveryear from (")
                            .append(amzQuery.substring(0, amzQuery.length() - 10)).append(") amazonStats ");
                } else {
                    amzMergeQuery.append("insert into ").append(tmpTablename).append("( week_no, week, date_range, aIMPRESSIONS, aCLICKS, aCOST, aORDERS, aREVENUE, group_name,yearoveryear ) ")
                            .append("  select amazonStats.week_no,amazonStats.Week,amazonStats.Date_Range,amazonStats.aimpressions,amazonStats.aclicks, amazonStats.acost,amazonStats.aOrders,amazonStats.aRevenue,group_name,yearoveryear from (")
                            .append(amzQuery.substring(0, amzQuery.length() - 10)).append(") amazonStats ");
                }
                autoReportDownloadDao.updateQuery(amzMergeQuery.toString());
            } else {
                if ("month_no".equals(durCondition)) {
                    amzSPMergeQuery.append("insert into ").append(tmpTablename).append("( week_no, week, YEAR, date_range, aIMPRESSIONS, aCLICKS, aCOST, aORDERS, aREVENUE, group_name,yearoveryear ) ")
                            .append("  select amazonStats.week_no,amazonStats.Week,amazonStats.year,amazonStats.Date_Range,amazonStats.aimpressions,amazonStats.aclicks, amazonStats.acost,amazonStats.aOrders,amazonStats.aRevenue,group_name,yearoveryear from (")
                            .append(amzSPQuery.substring(0, amzSPQuery.length() - 10)).append(") amazonStats ");

                    amzHSAMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                            .append(amzHSAQuery.substring(0, amzHSAQuery.length() - 10)).append(" ) amazonStats on ")
                            .append("tmp.week_no = amazonStats.week_no and tmp.week = amazonStats.week and tmp.year = amazonStats.year and tmp.Date_Range = amazonStats.Date_Range and tmp.yearoveryear = amazonStats.yearoveryear ")
                            .append(" SET ").append("tmp.hIMPRESSIONS = amazonStats.hIMPRESSIONS,tmp.hCLICKS = amazonStats.hCLICKS,tmp.hCOST= amazonStats.hCOST, tmp.hORDERS= amazonStats.hORDERS, tmp.hREVENUE= amazonStats.hREVENUE ");
                } else {
                    amzSPMergeQuery.append("insert into ").append(tmpTablename).append("( week_no, week, date_range, aIMPRESSIONS, aCLICKS, aCOST, aORDERS, aREVENUE, group_name,yearoveryear ) ")
                            .append("  select amazonStats.week_no,amazonStats.Week,amazonStats.Date_Range,amazonStats.aimpressions,amazonStats.aclicks, amazonStats.acost,amazonStats.aOrders,amazonStats.aRevenue,group_name,yearoveryear from (")
                            .append(amzSPQuery.substring(0, amzSPQuery.length() - 10)).append(") amazonStats ");

                    amzHSAMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                            .append(amzHSAQuery.substring(0, amzHSAQuery.length() - 10)).append(" ) amazonStats on ")
                            .append("tmp.week_no = amazonStats.week_no and tmp.week = amazonStats.week and tmp.yearoveryear = amazonStats.yearoveryear ")
                            .append(" SET ").append("tmp.hIMPRESSIONS = amazonStats.hIMPRESSIONS,tmp.hCLICKS = amazonStats.hCLICKS,tmp.hCOST= amazonStats.hCOST, tmp.hORDERS= amazonStats.hORDERS, tmp.hREVENUE= amazonStats.hREVENUE ");

                }
                autoReportDownloadDao.updateQuery(amzSPMergeQuery.toString());
                autoReportDownloadDao.updateQuery(amzHSAMergeQuery.toString());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public String getFacebookMonthlyPerformanceReport() {
        List<GoogleAnalyticsInformation> gaDetails = automationTemplateDao.getAnalyticsDefaultData(clientId);
        String columnIds = "1,2,3,4,5,9,8,10,11";
        try {
            fbAcc_id = Long.valueOf(templateInfo.getFbAccId());
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.FACEBOOK_PERFORMANCE_SUMMARY_REPORT_MONTHLY_NAME + "-" + mnRptNameSuffix + CommonConstants.XLSX;
            if (fbAcc_id != 0) {
                fbseAccId = accountDetailsDao.getAccountSeId(fbAcc_id);
            }
            automationCustomColumnses.clear();
            automationCustomColumnses = automationCustomColumnsDao.getGroupData("select * from lxr_reportsauto_customcolumns where client_id in(-1,-2," + clientInformation.get(0).getClientID() + ")");
            sqlFbColumnNames = prepareFacebookColumnNames(columnIds);

//            sqlColumnNames = sqlColumnNames.substring(0, sqlColumnNames.length() - 1);
            //Generation of First sheet i.e.,Facebook SMM Monthly Report
            totalFbQuery = new StringBuilder();
            LocalDate today = LocalDate.now();
            LocalDate monthStartDate = today.minusMonths(1).withDayOfMonth(1);;
            LOGGER.info(monthStartDate + "\n");

            LocalDate monthEndDate = monthStartDate.withDayOfMonth(monthStartDate.lengthOfMonth());

            LOGGER.info(monthEndDate);

            totalFbQuery.append("(select 'RW1' as curfmonth_no ,frevenue as totalfbrevenue,fcost as totalfbcost,forders as totalfborders,fid fbid,garevenue totalfbgarevenue ,gaorders as totalfbgaorders ,id as fbgaid, 0 as IS_MOM from (select frevenue,fcost,forders,fid from ( SELECT SUM(REVENUE) AS fREVENUE,SUM(COST) AS fCOST,SUM(ORDERS) AS fORDERS,2 AS fID  FROM ")
                    .append("(  select ifnull(sum(FACEBOOK_REVENUE),0) as revenue,sum(SPEND) as cost,sum(FACEBOOK_ORDERS) as orders from Facebook_Report_CampaignStats where   advert_account_id = ").append(fbseAccId).append(" and DATE_START between '").append(monthStartDate).append("' and '").append(monthEndDate).append("' ) mtd1 ) mtd2 ) facebook inner join")
                    .append("(select garevenue, gaorders,id from ( SELECT SUM(REVENUE) AS gaREVENUE,SUM(ORDERS) AS gaORDERS,2 AS ID FROM (select ifnull(sum(ga_revenue),0) as revenue,0 as cost,sum(ga_transactions) as orders,1 as id from lxr_reportsgastats where")
                    .append(" profile_id = ").append(gaDetails.get(0).getProfileId()).append(" and medium_type = 1 and search_engine_id = 4 and SE_DATE between '").append(monthStartDate).append("' and '").append(monthEndDate).append("' )  mtd1 ) mtd2) facebookga on facebookga.id = facebook.fid)");
            LOGGER.info("Query gives Total Facebook and Facebook Analytics Data ......" + totalFbQuery);

            getMonthlyQueries(tmpTablename, "509", AutoReportConstants.FACEBOOK, false);
            getFacebookMergeQueries(tmpTablename, "monthly");
            autoReportsStatsCol = new AutoReportsStatsInfo();
            finalTotals = new ArrayList();
            basicFbTotalSqlColumnNames = "ifnull(fimpressions,0) as impressions ,ifnull(fCLICKS,0) as clicks,ifnull(fCOST,0) as cost,ifnull(fORDERS,0) as orders,"
                    + "ifnull(fREVENUE,0) as revenue,ifnull(costperpurchase,0) as costperpurchase,ifnull(page_likes,0) as page_likes,ifnull(post_engagement,0) as post_engagement,ifnull(rbyc,0) as rbyc";
            LOGGER.info("basicTotalSqlColumnNames :" + basicFbTotalSqlColumnNames); //To set data to "finalTotals" object
            String basicTotalsQuery = "select " + basicFbTotalSqlColumnNames + ",group_name,month_no ,IS_MOM from " + tmpTablename + " order by month_no  desc";
            LOGGER.info("basicTotalsQuery :" + basicTotalsQuery);
            LOGGER.info("sqlFbColumnNames :" + sqlFbColumnNames);
//            LOGGER.info("sqlColumnNames :" + sqlColumnNames);
            finalStatsQuery = "select campaign_name,campaign_objective,results,reach,fIMPRESSIONS,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,"
                    + "rbyc from  " + tmpTablename + " where IS_MOM = 0 and campaign_name is not null ";
            LOGGER.info("Facebook SMM Monthly query........" + finalStatsQuery); // final query to generate report

            fbCampSum = "select 'Total' as campaign_name,   'Multiple' as campaign_objective, sum(results) as results,sum(reach) as reach,ifnull(sum(fIMPRESSIONS),0) as fIMPRESSIONS,sum(fCLICKS) as fCLICKS,sum(fCOST) as fCOST,sum(fORDERS) as fORDERS,sum(fREVENUE) as fREVENUE,sum(costperpurchase) as costperpurchase,"
                    + "sum(page_likes) as page_likes,sum(post_engagement) as post_engagement,sum(rbyc) as rbyc  from " + tmpTablename + " where is_mom = 0";
            finalTotals = autoReportDownloadDao.loadObjectsForFacebookTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);
            autoReportsStatsCol = autoReportDownloadDao.facebookMTDObjects(totalFbQuery.toString(), templateInfo); //To set MTD data
//            is_mom = 0;
            excelGenerator = setDataToExcelGenerator();

//            if (is_mom == true) {
            excelGenerator.setPrevYear(false);
//            }
            excelGenerator.setReportColNames("Campaign Name,Campaign Objective ,Results,Reach ," + reportFbColNames);
            excelGenerator.setReportName(AutoReportConstants.FACEBOOK_SEM_MONTHLY_REPORT);
            excelGenerator.setIsMonthlyReport(1);
            excelGenerator.setReportType(AutoReportConstants.FACEBOOK_REPORT_MONTHLY);
            excelGenerator.setLoopCnt(monthlyLoopCnt - 2);
            excelGenerator.generateNewXLSXReport();
            if (is_mom == false) {
                is_mom = true;
                excelGenerator.setIs_mom(is_mom);
                totalFbMomQuery = new StringBuilder();
//                  LocalDate today = LocalDate.now();

                LocalDate monthPrevStartDate = today.minusMonths(2).withDayOfMonth(1);;
                LOGGER.info(monthPrevStartDate + "\n");

                LocalDate monthPrevEndDate = monthPrevStartDate.withDayOfMonth(monthPrevStartDate.lengthOfMonth());

                String monthStart = monthPrevStartDate.toString();
//            System.out.println(monthStartDate + "\n");
                String[] dateParts = monthStart.split("-");
                int yearStrt = Integer.parseInt(dateParts[0]);
                int monthStrt = Integer.parseInt(dateParts[1]);
                int dayStrt = Integer.parseInt(dateParts[2]);
                fmonthName = CommonFunctions.getMonthName(monthStrt - 1);

                String monthEnd = monthPrevEndDate.toString();
                String[] datePart = monthEnd.split("-");
                int yearEnd = Integer.parseInt(datePart[0]);
                int montEnd = Integer.parseInt(datePart[1]);
                int dayEnd = Integer.parseInt(datePart[2]);
                tmonthName = CommonFunctions.getMonthName(montEnd - 1);
                dateRange = fmonthName + " " + dayStrt + "," + " " + yearStrt + "-" + tmonthName + " " + dayEnd + "," + " " + yearEnd;

                reportingDate = dateRange;

                totalFbMomQuery.append("(select 'RM1' as curfmonth_no ,frevenue as totalfbrevenue,fcost as totalfbcost,forders as totalfborders,fid fbid,garevenue totalfbgarevenue ,gaorders as totalfbgaorders ,id as fbgaid, 1 as IS_MOM from (select frevenue,fcost,forders,fid from ( SELECT SUM(REVENUE) AS fREVENUE,SUM(COST) AS fCOST,SUM(ORDERS) AS fORDERS,2 AS fID  FROM ")
                        .append("(  select ifnull(sum(FACEBOOK_REVENUE),0) as revenue,sum(SPEND) as cost,sum(FACEBOOK_ORDERS) as orders from Facebook_Report_CampaignStats where   advert_account_id = ").append(fbseAccId).append(" and DATE_START between '").append(monthPrevStartDate).append("' and '").append(monthPrevEndDate).append("' ) mtd1 ) mtd2 ) facebook inner join")
                        .append("(select garevenue, gaorders,id from ( SELECT SUM(REVENUE) AS gaREVENUE,SUM(ORDERS) AS gaORDERS,2 AS ID FROM (select ifnull(sum(ga_revenue),0) as revenue,0 as cost,sum(ga_transactions) as orders,1 as id from lxr_reportsgastats where")
                        .append(" profile_id = ").append(gaDetails.get(0).getProfileId()).append(" and medium_type = 1 and search_engine_id = 4 and SE_DATE between '").append(monthPrevStartDate).append("' and '").append(monthPrevEndDate).append("' )  mtd1 ) mtd2) facebookga on facebookga.id = facebook.fid)");
                autoReportsStatsCol = null;
                autoReportsStatsCol = autoReportDownloadDao.facebookMTDObjects(totalFbMomQuery.toString(), templateInfo);
                finalStatsQuery = "select date_range,reach,fIMPRESSIONS,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,"
                        + "rbyc from  " + tmpTablename + " where IS_MOM = 1 order by week_no desc";
                LOGGER.info("Facebook MOM query........" + finalStatsQuery); // final query to generate report
                String basicTotalsMomQuery = "select " + basicFbTotalSqlColumnNames + ",group_name,month_no ,IS_MOM from " + tmpTablename + " where is_mom=1 order by week_no  desc";
                LOGGER.info("basicTotalsMomQuery :" + basicTotalsMomQuery);
                finalTotals.clear();
                finalTotals = autoReportDownloadDao.loadObjectsForFacebookTotals(basicTotalsMomQuery, finalTotals, colNamesWithTotal);
                excelGenerator.setSql(finalStatsQuery);
                excelGenerator.setAutoReportsStatsCol(autoReportsStatsCol);
//                fbCampDiff = "";
//                excelGenerator.setFbCampDiff(fbCampDiff);
                excelGenerator.setReportingDate(reportingDate);
                excelGenerator.setReportColNames("Date Range,Reach," + reportFbColNames);
                excelGenerator.generateNewXLSXReport();
            }
        } catch (NumberFormatException ex) {
            LOGGER.info(ex);
        } finally {
            autoReportDownloadDao.dropTable(tmpTablename);
            LOGGER.info("Temporary Successfully Deleted....");
        }
        return "";
    }

    public String getFacebookWeeklyPerformanceReport() {
        try {
            String columnIds = "1,2,3,4,5,9,8,10,11";
            fbAcc_id = Long.valueOf(templateInfo.getFbAccId());
            tmpTablename = "tmp_AR_" + templateId + "_" + cal.getTimeInMillis();
            fileName = reportsPath + AutoReportConstants.WEEKLY_FOLDER_NAME + "/" + clientId + "/" + templateInfo.getTemplateId() + "/" + rptNamePrefix + "-" + AutoReportConstants.FACEBOOK_PERFORMANCE_SUMMARY_REPORT_WEEKLY_NAME + "-" + wkRptNameSuffix + CommonConstants.XLSX;
            if (fbAcc_id != 0) {
                fbseAccId = accountDetailsDao.getAccountSeId(fbAcc_id);
            }
            automationCustomColumnses.clear();
            automationCustomColumnses = automationCustomColumnsDao.getGroupData("select * from lxr_reportsauto_customcolumns where client_id in(-1,-2," + clientInformation.get(0).getClientID() + ")");
            sqlFbColumnNames = prepareFacebookColumnNames(columnIds);
//            sqlColumnNames = sqlColumnNames.substring(0, sqlColumnNames.length() - 1);
//            String columnIds = "1,2,3,4,5,9,8,10,11";
//            String prepareColumnNames1 = prepareFacebookColumnNames(columnIds);
            getWeeklyQueries(tmpTablename, "509", AutoReportConstants.FACEBOOK_REPORT_WEEKLY, false);
            getFacebookMergeQueries(tmpTablename, "weekly");

            autoReportsStatsCol = new AutoReportsStatsInfo();
            finalTotals = new ArrayList();
            basicFbTotalSqlColumnNames = "ifnull(fimpressions,0) as impressions ,ifnull(fCLICKS,0) as clicks,ifnull(fCOST,0) as cost,ifnull(fORDERS,0) as orders,"
                    + "ifnull(fREVENUE,0) as revenue,ifnull(costperpurchase,0) as costperpurchase,ifnull(page_likes,0) as page_likes,ifnull(post_engagement,0) as post_engagement,ifnull(rbyc,0) as rbyc";
            LOGGER.info("basicTotalSqlColumnNames :" + basicFbTotalSqlColumnNames); //To set data to "finalTotals" object
            String basicTotalsQuery = "select " + basicFbTotalSqlColumnNames + ",group_name,month_no ,IS_MOM from " + tmpTablename + " order by month_no  desc";
            LOGGER.info("basicTotalsQuery :" + basicTotalsQuery);
            LOGGER.info("sqlFbColumnNames :" + sqlFbColumnNames);
//            LOGGER.info("sqlColumnNames :" + sqlColumnNames);
            finalStatsQuery = "select campaign_name,campaign_objective,results,reach,fIMPRESSIONS,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,"
                    + "rbyc from  " + tmpTablename + " where IS_MOM = 0 and campaign_name is not null ";
            LOGGER.info("Facebook SMM Weekly query........" + finalStatsQuery); // final query to generate report

            fbCampSum = "select 'Total' as campaign_name,   'Multiple' as campaign_objective, sum(results) as results,sum(reach) as reach,ifnull(sum(fIMPRESSIONS),0) as fIMPRESSIONS,sum(fCLICKS) as fCLICKS,sum(fCOST) as fCOST,sum(fORDERS) as fORDERS,sum(fREVENUE) as fREVENUE,sum(costperpurchase) as costperpurchase,"
                    + "sum(page_likes) as page_likes,sum(post_engagement) as post_engagement,sum(rbyc) as rbyc  from " + tmpTablename + " where is_mom = 0";
            finalTotals = autoReportDownloadDao.loadObjectsForFacebookTotals(basicTotalsQuery, finalTotals, colNamesWithTotal);
            autoReportsStatsCol = autoReportDownloadDao.facebookMTDObjects(totalFbQuery.toString(), templateInfo); //To set MTD data
//            is_mom = 0;
            if (!isSigned) {
                excelGenerator = setDataToExcelGenerator();

                excelGenerator.setPrevYear(false);
                excelGenerator.setReportColNames("Campaign Name,Campaign Objective ,Results,Reach ," + reportFbColNames);
                excelGenerator.setReportName(AutoReportConstants.FACEBOOK_SEM_WEEKLY_REPORT);
                excelGenerator.setIsMonthlyReport(1);
                excelGenerator.setReportType(AutoReportConstants.FACEBOOK_REPORT_WEEKLY);
                excelGenerator.setLoopCnt(weeklyLoopCnt - 2);
                excelGenerator.generateNewXLSXReport();
            }
            if (is_mom == false) {
                is_mom = true;
                autoReportsStatsCol = null;
                autoReportsStatsCol = autoReportDownloadDao.facebookMTDObjects(totalWowQuery.toString(), templateInfo);
                finalStatsQuery = "select date_range,reach,fIMPRESSIONS,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,"
                        + "rbyc from  " + tmpTablename + " where IS_MOM = 1 order by week_no desc";
                LOGGER.info("Facebook WOW query........" + finalStatsQuery); // final query to generate report
                String basicTotalsMomQuery = "select " + basicFbTotalSqlColumnNames + ",group_name,month_no ,IS_MOM from " + tmpTablename + " where is_mom=1 order by week_no  desc";
                LOGGER.info("basicTotalsMomQuery :" + basicTotalsMomQuery);
                finalTotals.clear();
                finalTotals = autoReportDownloadDao.loadObjectsForFacebookTotals(basicTotalsMomQuery, finalTotals, colNamesWithTotal);
                if (!isSigned) {
                    excelGenerator.setIs_mom(is_mom);
                    excelGenerator.setSql(finalStatsQuery);
                    excelGenerator.setAutoReportsStatsCol(autoReportsStatsCol);

                    excelGenerator.setReportingDate(reportingDate);
                    excelGenerator.setReportColNames("Date Range,Reach," + reportFbColNames);
                    excelGenerator.generateNewXLSXReport();
                }
            }
        } catch (NumberFormatException ex) {
            LOGGER.info(ex);
            LOGGER.info("Exception" + ex);
        } finally {
            if (!isSigned) {
                autoReportDownloadDao.dropTable(tmpTablename);
                LOGGER.info("Temporary Successfully Deleted....");
            }
        }
        return finalStatsQuery;
    }

    public void getFacebookWeeklyOrMonthlyQueries(int i, String grpIds, String type) {
        List<GoogleAnalyticsInformation> geDetails = automationTemplateDao.getAnalyticsDefaultData(clientId);
        //The Below Query is to fetch only the facebook data for first sheet 
        fbQuery.append(" ( select 'RW").append(i).append(" ' month_no,Date_Range,CAMPAIGN_NAME,CAMPAIGN_ID,OBJECTIVE,RESULTS AS RESULTS,REACH AS REACH ,IMPRESSIONS as fimpressions,CLICKS as CLICKS,")
                .append(" COST as COST,FACEBOOK_ORDERS as FACEBOOK_ORDERS,FACEBOOK_REVENUE as FACEBOOK_REVENUE,COST_PER_PURCHASE AS COST_PER_PURCHASE,PAGE_LIKES AS PAGE_LIKES, POST_ENGAGEMENT AS POST_ENGAGEMENT,RbyC,group_name   FROM ")
                .append("( select DATE_RANGE,CAMPAIGN_NAME , OBJECTIVE,RESULTS,REACH,IMPRESSIONS, CLICKS,COST,b1.CAMPAIGN_ID,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,grpnames.group_name from ")
                .append("( select 'RW").append(i).append(" ' as Month,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS, ")
                .append(" sum(SPEND) as COST,sum(FACEBOOK_ORDERS) as FACEBOOK_ORDERS,sum(FACEBOOK_REVENUE) as FACEBOOK_REVENUE,SUM(COST_PER_PURCHASE) AS COST_PER_PURCHASE, SUM(PAGE_LIKES) AS PAGE_LIKES, SUM(POST_ENGAGEMENT) AS POST_ENGAGEMENT, CASE SUM(SPEND) WHEN 0 THEN 0 ELSE (SUM(FACEBOOK_REVENUE) / SUM(SPEND)) END AS RbyC,CAMPAIGN_NAME,OBJECTIVE,SUM(REACH) AS REACH ,SUM(RESULTS) AS RESULTS ,CAMPAIGN_ID from ").append(fbStatsTableName).append(" where ")
                .append(" ADVERT_ACCOUNT_ID = ").append(fbseAccId).append(" and ").append("DATE_START between '").append(fbFromDate).append("' and '").append(fbToDate).append("'  group by CAMPAIGN_ID ) b1 join ")
                .append("(select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                .append("and group_id = ").append(grpIds).append(" and account_id = ").append(fbAcc_id).append(" and CLIENT_ID = ").append(clientId)
                .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID))  k group by CAMPAIGN_ID ) fbStats");

        if (type == "monthly") {
            for (int h = 1; h < 4; h++) {
                LocalDate today = LocalDate.now();
                LocalDate monthStartDate = today.minusMonths(h).withDayOfMonth(1);
                String monthStart = monthStartDate.toString();
                String[] dateParts = monthStart.split("-");
                int yearStrt = Integer.parseInt(dateParts[0]);
                int monthStrt = Integer.parseInt(dateParts[1]);
                int dayStrt = Integer.parseInt(dateParts[2]);
                fmonthName = CommonFunctions.getMonthName(monthStrt - 1);

                LocalDate monthEndDate = monthStartDate.withDayOfMonth(monthStartDate.lengthOfMonth());
                String monthEnd = monthEndDate.toString();
                String[] datePart = monthEnd.split("-");
                int yearEnd = Integer.parseInt(datePart[0]);
                int montEnd = Integer.parseInt(datePart[1]);
                int dayEnd = Integer.parseInt(datePart[2]);
                tmonthName = CommonFunctions.getMonthName(montEnd - 1);
                dateRange = fmonthName + " " + dayStrt + "," + " " + yearStrt + "-" + tmonthName + " " + dayEnd + "," + " " + yearEnd;

                fbMomQuery.append("  select 'RM1' AS month_no,").append(h).append(" as week_no, DATE_RANGE,RESULTS,REACH,fimpressions,CLICKS, COST ,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,group_name   FROM ")
                        .append("(select DATE_RANGE,SUM(RESULTS) AS RESULTS,SUM(REACH) AS REACH,SUM(IMPRESSIONS) as fimpressions, SUM(CLICKS) as CLICKS,SUM(COST)  as COST,SUM(FACEBOOK_ORDERS)  as FACEBOOK_ORDERS,SUM(FACEBOOK_REVENUE) as FACEBOOK_REVENUE,SUM(COST_PER_PURCHASE)  AS COST_PER_PURCHASE,SUM(PAGE_LIKES) AS PAGE_LIKES,SUM(POST_ENGAGEMENT) AS POST_ENGAGEMENT,AVG(RbyC) as RbyC,grpnames.group_name from")
                        .append("(select 'RM1 ' as Month,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS,sum(SPEND) as COST,sum(FACEBOOK_ORDERS) as FACEBOOK_ORDERS,sum(FACEBOOK_REVENUE) as FACEBOOK_REVENUE,SUM(COST_PER_PURCHASE) AS COST_PER_PURCHASE, SUM(PAGE_LIKES) AS PAGE_LIKES, SUM(POST_ENGAGEMENT) AS POST_ENGAGEMENT, CASE SUM(SPEND) WHEN 0 THEN  0 ELSE (SUM(FACEBOOK_REVENUE) / SUM(SPEND)) END AS RbyC,CAMPAIGN_NAME,OBJECTIVE,SUM(REACH) AS REACH ,SUM(RESULTS) AS RESULTS,CAMPAIGN_ID from ")
                        .append(" Facebook_Report_CampaignStats where  ADVERT_ACCOUNT_ID = ").append(fbseAccId).append(" and ").append("DATE_START between '").append(monthStartDate).append("' and '").append(monthEndDate).append("'  group by CAMPAIGN_ID ) b1 join ")
                        .append(" (select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append("and group_id = ").append(grpIds).append(" and account_id = ").append(fbAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID))  k  union all");

                if (h == 2) {
                    totalMomQuery.append("(select 'RM1' as curfmonth_no ,frevenue as totalfbrevenue,fcost as totalfbcost,forders as totalfborders,fid fbid,garevenue totalfbgarevenue ,gaorders as totalfbgaorders ,id as fbgaid, 1 as IS_MOM from (select frevenue,fcost,forders,fid from ( SELECT SUM(REVENUE) AS fREVENUE,SUM(COST) AS fCOST,SUM(ORDERS) AS fORDERS,2 AS fID  FROM ")
                            .append("(  select ifnull(sum(FACEBOOK_REVENUE),0) as revenue,sum(SPEND) as cost,sum(FACEBOOK_ORDERS) as orders from Facebook_Report_CampaignStats where   advert_account_id = ").append(fbseAccId).append(" and DATE_START between '").append(monthStartDate).append("' and '").append(monthEndDate).append("' ) mtd1 ) mtd2 ) facebook inner join")
                            .append("(select garevenue, gaorders,id from ( SELECT SUM(REVENUE) AS gaREVENUE,SUM(ORDERS) AS gaORDERS,2 AS ID FROM (select ifnull(sum(ga_revenue),0) as revenue,0 as cost,sum(ga_transactions) as orders,1 as id from lxr_reportsgastats where")
                            .append(" profile_id = ").append(geDetails.get(0).getProfileId()).append(" and medium_type = 1 and search_engine_id = 4 and SE_DATE between '").append(monthStartDate).append("' and '").append(monthEndDate).append("' )  mtd1 ) mtd2) facebookga on facebookga.id = facebook.fid)");
                }

            }
        }
        if (type == "weekly") {

            LocalDate weekStartDate = LocalDate.parse(fbFromDate);
            LocalDate weekEndDate = LocalDate.parse(fbToDate);
            for (int h = 1; h < 4; h++) {
                LocalDate startDate, endDate;
                startDate = weekStartDate;
                endDate = weekEndDate;
                String weekStart = startDate.toString();
                String[] dateParts = weekStart.split("-");
                int yearStrt = Integer.parseInt(dateParts[0]);
                int monthStrt = Integer.parseInt(dateParts[1]);
                int dayStrt = Integer.parseInt(dateParts[2]);
                String fmonthName = CommonFunctions.getMonthName(monthStrt - 1);

                String weekStart1 = endDate.toString();
                String[] dateParts1 = weekStart1.split("-");
                int yearStrt1 = Integer.parseInt(dateParts1[0]);
                int monthStrt1 = Integer.parseInt(dateParts1[1]);
                int dayStrt1 = Integer.parseInt(dateParts1[2]);
                String fmonthName1 = CommonFunctions.getMonthName(monthStrt1 - 1);

                String dateRange = fmonthName + " " + dayStrt + "," + " " + yearStrt + "-" + fmonthName1 + " " + dayStrt1 + "," + " " + yearStrt1;;

                fbWowQuery.append("  select 'RM1' AS month_no,").append(h).append(" as week_no, DATE_RANGE,RESULTS,REACH,fimpressions,CLICKS, COST ,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,group_name   FROM ")
                        .append("(select DATE_RANGE,SUM(RESULTS) AS RESULTS,SUM(REACH) AS REACH,SUM(IMPRESSIONS) as fimpressions, SUM(CLICKS) as CLICKS,SUM(COST)  as COST,SUM(FACEBOOK_ORDERS)  as FACEBOOK_ORDERS,SUM(FACEBOOK_REVENUE) as FACEBOOK_REVENUE,SUM(COST_PER_PURCHASE)  AS COST_PER_PURCHASE,SUM(PAGE_LIKES) AS PAGE_LIKES,SUM(POST_ENGAGEMENT) AS POST_ENGAGEMENT,AVG(RbyC) as RbyC,grpnames.group_name from")
                        .append("(select 'RM1 ' as Month,'").append(dateRange).append(" ' as Date_Range, sum(IMPRESSIONS) as IMPRESSIONS,sum(CLICKS) as CLICKS,sum(SPEND) as COST,sum(FACEBOOK_ORDERS) as FACEBOOK_ORDERS,sum(FACEBOOK_REVENUE) as FACEBOOK_REVENUE,SUM(COST_PER_PURCHASE) AS COST_PER_PURCHASE, SUM(PAGE_LIKES) AS PAGE_LIKES, SUM(POST_ENGAGEMENT) AS POST_ENGAGEMENT, CASE SUM(SPEND) WHEN 0 THEN  0 ELSE (SUM(FACEBOOK_REVENUE) / SUM(SPEND)) END AS RbyC,CAMPAIGN_NAME,OBJECTIVE,SUM(REACH) AS REACH ,SUM(RESULTS) AS RESULTS,CAMPAIGN_ID from ")
                        .append(" Facebook_Report_CampaignStats where  ADVERT_ACCOUNT_ID = ").append(fbseAccId).append(" and ").append("DATE_START between '").append(startDate).append("' and '").append(endDate).append("'  group by CAMPAIGN_ID ) b1 join ")
                        .append(" (select CAMPAIGN_ID,group_id,group_name from ( (select CAMPAIGN_ID,group_id from reports_automation_groups where component_level=1 ")
                        .append("and group_id = ").append(grpIds).append(" and account_id = ").append(fbAcc_id).append(" and CLIENT_ID = ").append(clientId)
                        .append(" ) grps join  (select group_id as id,group_name from lxr_kpi_group_master where ")
                        .append(" group_id = ").append(grpIds).append(" ) mstr  on grps.group_id = mstr.id )) grpnames on (grpnames.CAMPAIGN_ID = b1.CAMPAIGN_ID))  k  union all");
                if (h == 1) {
                    totalFbQuery.append("(select 'RW1' as curfmonth_no ,frevenue as totalfbrevenue,fcost as totalfbcost,forders as totalfborders,fid fbid,garevenue totalfbgarevenue ,gaorders as totalfbgaorders ,id as fbgaid, 0 as IS_MOM from (select frevenue,fcost,forders,fid from ( SELECT SUM(REVENUE) AS fREVENUE,SUM(COST) AS fCOST,SUM(ORDERS) AS fORDERS,2 AS fID  FROM ")
                            .append("(  select ifnull(sum(FACEBOOK_REVENUE),0) as revenue,sum(SPEND) as cost,sum(FACEBOOK_ORDERS) as orders from Facebook_Report_CampaignStats where   advert_account_id = ").append(fbseAccId).append(" and DATE_START between '").append(startDate).append("' and '").append(endDate).append("' ) mtd1 ) mtd2 ) facebook inner join")
                            .append("(select garevenue, gaorders,id from ( SELECT SUM(REVENUE) AS gaREVENUE,SUM(ORDERS) AS gaORDERS,2 AS ID FROM (select ifnull(sum(ga_revenue),0) as revenue,0 as cost,sum(ga_transactions) as orders,1 as id from lxr_reportsgastats where")
                            .append(" profile_id = ").append(geDetails.get(0).getProfileId()).append(" and medium_type = 1 and search_engine_id = 4 and SE_DATE between '").append(startDate).append("' and '").append(endDate).append("' )  mtd1 ) mtd2) facebookga on facebookga.id = facebook.fid)");
                }
                if (h == 2) {
                    totalWowQuery.append("(select 'RM1' as curfmonth_no ,frevenue as totalfbrevenue,fcost as totalfbcost,forders as totalfborders,fid fbid,garevenue totalfbgarevenue ,gaorders as totalfbgaorders ,id as fbgaid, 1 as IS_MOM from (select frevenue,fcost,forders,fid from ( SELECT SUM(REVENUE) AS fREVENUE,SUM(COST) AS fCOST,SUM(ORDERS) AS fORDERS,2 AS fID  FROM ")
                            .append("(  select ifnull(sum(FACEBOOK_REVENUE),0) as revenue,sum(SPEND) as cost,sum(FACEBOOK_ORDERS) as orders from Facebook_Report_CampaignStats where   advert_account_id = ").append(fbseAccId).append(" and DATE_START between '").append(startDate).append("' and '").append(endDate).append("' ) mtd1 ) mtd2 ) facebook inner join")
                            .append("(select garevenue, gaorders,id from ( SELECT SUM(REVENUE) AS gaREVENUE,SUM(ORDERS) AS gaORDERS,2 AS ID FROM (select ifnull(sum(ga_revenue),0) as revenue,0 as cost,sum(ga_transactions) as orders,1 as id from lxr_reportsgastats where")
                            .append(" profile_id = ").append(geDetails.get(0).getProfileId()).append(" and medium_type = 1 and search_engine_id = 4 and SE_DATE between '").append(startDate).append("' and '").append(endDate).append("' )  mtd1 ) mtd2) facebookga on facebookga.id = facebook.fid)");
                }
                weekStartDate = startDate.minusDays(7);

                weekEndDate = endDate.minusDays(7);

            }
        }
    }

    private void getFacebookMergeQueries(String tmpTablename, String type) {

        if (type == "monthly") {
            fbMergeQuery.append("insert into ").append(tmpTablename).append("(month_no,Date_Range,CAMPAIGN_NAME,CAMPAIGN_ID,campaign_objective,results,reach,fimpressions,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,rbyc,group_name,curfmonth_no,totalfbrevenue,totalfbcost,totalfborders,fbid,totalfbgarevenue,totalfbgaorders,fbgaid,IS_MOM ) ")
                    .append("select month_no ,Date_Range,CAMPAIGN_NAME,CAMPAIGN_ID,OBJECTIVE,RESULTS,REACH,fimpressions,CLICKS,COST,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,group_name,present.curfmonth_no,present.totalfbrevenue,present.totalfbcost,present.totalfborders,present.fbid,present.totalfbgarevenue,present.totalfbgaorders,present.fbgaid,present.IS_MOM from")
                    .append(fbQuery.substring(0, fbQuery.length())).append(" join ").append(totalFbQuery).append(" present  on fbStats.month_no = present.curfmonth_no");
            autoReportDownloadDao.updateQuery(fbMergeQuery.toString());

            fbMomMergeQuery.append("insert into ").append(tmpTablename).append("(month_no,week_no,Date_Range,results,reach,fimpressions,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,rbyc,group_name,curfmonth_no,totalfbrevenue,totalfbcost,totalfborders,fbid,totalfbgarevenue,totalfbgaorders,fbgaid,IS_MOM ) ")
                    .append("select month_no , week_no, Date_Range,RESULTS,REACH,fimpressions,CLICKS,COST,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,group_name,present.curfmonth_no,present.totalfbrevenue,present.totalfbcost,present.totalfborders,present.fbid,present.totalfbgarevenue,present.totalfbgaorders,present.fbgaid,present.IS_MOM from (")
                    .append(fbMomQuery.substring(0, fbMomQuery.length() - 10)).append(" )fbStats join ").append(totalMomQuery).append(" present  on fbStats.month_no = present.curfmonth_no");
            autoReportDownloadDao.updateQuery(fbMomMergeQuery.toString());
        }
        if (type == "weekly") {
            fbMergeQuery.append("insert into ").append(tmpTablename).append("(month_no,Date_Range,CAMPAIGN_NAME,CAMPAIGN_ID,campaign_objective,results,reach,fimpressions,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,rbyc,group_name,curfmonth_no,totalfbrevenue,totalfbcost,totalfborders,fbid,totalfbgarevenue,totalfbgaorders,fbgaid,IS_MOM ) ")
                    .append("select month_no ,Date_Range,CAMPAIGN_NAME,CAMPAIGN_ID,OBJECTIVE,RESULTS,REACH,fimpressions,CLICKS,COST,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,group_name,present.curfmonth_no,present.totalfbrevenue,present.totalfbcost,present.totalfborders,present.fbid,present.totalfbgarevenue,present.totalfbgaorders,present.fbgaid,present.IS_MOM from")
                    .append(fbQuery.substring(0, fbQuery.length())).append(" join ").append(totalFbQuery).append(" present  on fbStats.month_no = present.curfmonth_no");
            autoReportDownloadDao.updateQuery(fbMergeQuery.toString());

            fbWomMergeQuery.append("insert into ").append(tmpTablename).append("(month_no,week_no,Date_Range,results,reach,fimpressions,fCLICKS,fCOST,fORDERS,fREVENUE,costperpurchase,page_likes,post_engagement,rbyc,group_name,curfmonth_no,totalfbrevenue,totalfbcost,totalfborders,fbid,totalfbgarevenue,totalfbgaorders,fbgaid,IS_MOM ) ")
                    .append("select month_no , week_no, Date_Range,RESULTS,REACH,fimpressions,CLICKS,COST,FACEBOOK_ORDERS,FACEBOOK_REVENUE,COST_PER_PURCHASE,PAGE_LIKES,POST_ENGAGEMENT,RbyC,group_name,present.curfmonth_no,present.totalfbrevenue,present.totalfbcost,present.totalfborders,present.fbid,present.totalfbgarevenue,present.totalfbgaorders,present.fbgaid,present.IS_MOM from (")
                    .append(fbWowQuery.substring(0, fbWowQuery.length() - 10)).append(" )fbStats join ").append(totalWowQuery).append(" present  on fbStats.month_no = present.curfmonth_no");
            autoReportDownloadDao.updateQuery(fbWomMergeQuery.toString());
        }

    }

    private void getCustomGroupMonthlyOrWeeklyMergeQueries(String tmpTablename) {
        //New Conditions
        if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id == 0) {// Only GLE
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());
        } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id == 0) {// Only MSN

            msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnQuery.substring(0, msnQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

        } else if (gAcc_id == 0 && mAcc_id == 0 && yahAcc_id != 0) {// Only YG

            yahGemMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,yavg_pos,group_name,yearoveryear) ").append(yahGemQuery.substring(0, yahGemQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,yimpressions,yclicks,ycost,yorders,yrevenue,yavg_pos,group_name,yearoveryear) ").append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id == 0) {// Gle & MSN
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            msnMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnQuery.substring(0, msnQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear and tmp.date_range = msnStats.Date_Range and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos");
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear and tmp.date_range = msnStats.Date_Range and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos ");
            autoReportDownloadDao.updateQuery(msnYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id == 0 && yahAcc_id != 0) {// GLE & YG         
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.date_range = yahGemStats.Date_Range and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear and tmp.date_range = yahGemStats.Date_Range and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id == 0 && mAcc_id != 0 && yahAcc_id != 0) {// MSN & YG            
            msnMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnQuery.substring(0, msnQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,mimpressions,mclicks,mcost,morders,mrevenue,mavg_pos,group_name,yearoveryear) ").append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(msnYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear  and tmp.date_range = yahGemStats.Date_Range and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear  and tmp.date_range = yahGemStats.Date_Range and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());

        } else if (gAcc_id != 0 && mAcc_id != 0 && yahAcc_id != 0) {// GLE + MSN + YG 
            gleMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleQuery.substring(0, gleQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleMergeQuery.toString());

            gleYOYMergeQuery.append("insert into ").append(tmpTablename).append("(week_no,week,year,date_range,gimpressions,gclicks,gcost,gorders,grevenue,gavg_pos,group_name,yearoveryear) ").append(gleYOYQuery.substring(0, gleYOYQuery.length() - 10));
            tabledatainserted = autoReportDownloadDao.executeQuery(gleYOYMergeQuery.toString());

            msnMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnQuery.substring(0, msnQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear  and tmp.date_range = msnStats.Date_Range and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos");
            autoReportDownloadDao.updateQuery(msnMergeQuery.toString());

            msnYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(msnYOYQuery.substring(0, msnYOYQuery.length() - 10)).append(" ) msnStats on ")
                    .append(" tmp.week_no = msnStats.week_no and tmp.week = msnStats.week and tmp.yearoveryear = msnStats.yearoveryear  and tmp.date_range = msnStats.Date_Range and tmp.group_name = msnStats.group_name ")
                    .append(" SET ").append("tmp.mimpressions = msnStats.mimpressions, tmp.mclicks=msnStats.mclicks, tmp.mcost=msnStats.mcost, tmp.morders=msnStats.morders, tmp.mrevenue=msnStats.mrevenue, tmp.mavg_pos=msnStats.mavg_pos ");
            autoReportDownloadDao.updateQuery(msnYOYMergeQuery.toString());

            yahGemMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemQuery.substring(0, yahGemQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear  and tmp.date_range = yahGemStats.Date_Range and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemMergeQuery.toString());

            yahGemYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                    .append(yahGemYOYQuery.substring(0, yahGemYOYQuery.length() - 10)).append(" ) yahGemStats on ")
                    .append(" tmp.week_no = yahGemStats.week_no and tmp.week = yahGemStats.week and tmp.yearoveryear = yahGemStats.yearoveryear  and tmp.date_range = yahGemStats.Date_Range and tmp.group_name = yahGemStats.group_name ")
                    .append(" SET ").append(" tmp.yimpressions = yahGemStats.yimpressions,tmp.yclicks = yahGemStats.yclicks,tmp.ycost = yahGemStats.ycost,tmp.yorders = yahGemStats.yorders,tmp.yrevenue = yahGemStats.yrevenue,tmp.yavg_pos = yahGemStats.yavg_pos ");
            autoReportDownloadDao.updateQuery(yahGemYOYMergeQuery.toString());
        }
        // GA Merge Queries
        if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 1) {  // GA Total Revenue
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {
                gaGleMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear  and tmp.date_range = gagleStats.Date_Range and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost, tmp.gagleOrders= gagleStats.gagleOrders,tmp.gagleRevenue= gagleStats.gagleRevenue");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());

                gaGleYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and  tmp.yearoveryear = gagleStats.yearoveryear  and tmp.date_range = gagleStats.Date_Range and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost,"
                        + " tmp.gagleOrders= gagleStats.gagleOrders,tmp.gagleRevenue= gagleStats.gagleRevenue");

                autoReportDownloadDao.updateQuery(gaGleYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {
                gaMsnMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear  and tmp.date_range = gamsnStats.Date_Range and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnRevenue= gamsnStats.gamsnRevenue");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());

                gaMsnYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear  and tmp.date_range = gamsnStats.Date_Range and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnRevenue= gamsnStats.gamsnRevenue");
                autoReportDownloadDao.updateQuery(gaMsnYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                gaYahGemMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear  and tmp.date_range = gayahStats.Date_Range and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahRevenue= gayahStats.gayahRevenue");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());

                gaYahGemYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear  and tmp.date_range = gayahStats.Date_Range and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahRevenue= gayahStats.gayahRevenue");
                autoReportDownloadDao.updateQuery(gaYahGemYOYMergeQuery.toString());
            }
        }

        if (templateInfo.getGoogleAnalytics() != 0 && templateInfo.getRevenueType() == 2) {  // GA Product Revenue
            if ((templateInfo.getGoogleAnalytics() == 1 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 6) && gAcc_id != 0) {
                gaGleMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleQuery.substring(0, gaGleQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear  and tmp.date_range = gagleStats.Date_Range and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost, "
                        + "tmp.gagleOrders= gagleStats.gagleOrders,tmp.gaglePRevenue= gagleStats.gaglePRevenue, tmp.gagletax = gagleStats.gagletax, tmp.gagleshipping = gagleStats.gagleshipping");
                autoReportDownloadDao.updateQuery(gaGleMergeQuery.toString());

                gaGleYOYMergeQuery.append(" UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaGleYOYQuery.substring(0, gaGleYOYQuery.length() - 10)).append(" ) gagleStats ON ")
                        .append(" tmp.week_no = gagleStats.week_no and tmp.week = gagleStats.week and tmp.yearoveryear = gagleStats.yearoveryear  and tmp.date_range = gagleStats.Date_Range and tmp.group_name = gagleStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gagleStats.gaimpressions,tmp.gaclicks= gagleStats.gaclicks,tmp.gacost= gagleStats.gacost,"
                        + " tmp.gagleOrders= gagleStats.gagleOrders,tmp.gaglePRevenue= gagleStats.gaglePRevenue, tmp.gagletax = gagleStats.gagletax, tmp.gagleshipping = gagleStats.gagleshipping");

                autoReportDownloadDao.updateQuery(gaGleYOYMergeQuery.toString());
            }
//            if (mAcc_id != 0) 
            if ((templateInfo.getGoogleAnalytics() == 2 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 5 || templateInfo.getGoogleAnalytics() == 6) && mAcc_id != 0) {
                gaMsnMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnQuery.substring(0, gaMsnQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear and tmp.date_range = gamsnStats.Date_Range and tmp.group_name = gamsnStats.group_name  ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnPRevenue= gamsnStats.gamsnPRevenue, tmp.gamsnTax = gamsnStats.gamsnTax, tmp.gamsnShipping = gamsnStats.gamsnShipping");
                autoReportDownloadDao.updateQuery(gaMsnMergeQuery.toString());

                gaMsnYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaMsnYOYQuery.substring(0, gaMsnYOYQuery.length() - 10)).append(" ) gamsnStats on ")
                        .append(" tmp.week_no = gamsnStats.week_no and tmp.week = gamsnStats.week and tmp.yearoveryear = gamsnStats.yearoveryear and tmp.date_range = gamsnStats.Date_Range and tmp.group_name = gamsnStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gamsnStats.gaimpressions,tmp.gaclicks= gamsnStats.gaclicks,tmp.gacost= gamsnStats.gacost,"
                        + " tmp.gamsnOrders= gamsnStats.gamsnOrders,tmp.gamsnPRevenue= gamsnStats.gamsnPRevenue,tmp.gamsnTax = gamsnStats.gamsnTax, tmp.gamsnShipping = gamsnStats.gamsnShipping");
                autoReportDownloadDao.updateQuery(gaMsnYOYMergeQuery.toString());
            }
            if ((templateInfo.getGoogleAnalytics() == 10 || templateInfo.getGoogleAnalytics() == 3 || templateInfo.getGoogleAnalytics() == 4 || templateInfo.getGoogleAnalytics() == 5) && yahAcc_id != 0) {
                gaYahGemMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemQuery.substring(0, gaYahGemQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear and tmp.date_range = gayahStats.Date_Range and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahPRevenue= gayahStats.gayahPRevenue, tmp.gayahtax=gayahStats.gayahtax, tmp.gayahshipping= gayahStats.gayahshipping");
                autoReportDownloadDao.updateQuery(gaYahGemMergeQuery.toString());

                gaYahGemYOYMergeQuery.append("UPDATE ").append(tmpTablename).append(" tmp INNER JOIN (")
                        .append(gaYahGemYOYQuery.substring(0, gaYahGemYOYQuery.length() - 10)).append(" ) gayahStats on ")
                        .append(" tmp.week_no = gayahStats.week_no and tmp.week = gayahStats.week and tmp.yearoveryear = gayahStats.yearoveryear and tmp.date_range = gayahStats.Date_Range and tmp.group_name = gayahStats.group_name ")
                        .append(" SET ").append("tmp.gaimpressions= gayahStats.gaimpressions,tmp.gaclicks= gayahStats.gaclicks,tmp.gacost= gayahStats.gacost,"
                        + "tmp.gayahOrders= gayahStats.gayahOrders,tmp.gayahPRevenue= gayahStats.gayahPRevenue,tmp.gayahtax=gayahStats.gayahtax, tmp.gayahshipping= gayahStats.gayahshipping");
                autoReportDownloadDao.updateQuery(gaYahGemYOYMergeQuery.toString());
            }
        }
    }
}


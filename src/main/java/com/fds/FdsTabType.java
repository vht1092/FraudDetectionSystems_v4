package com.fds;

import com.fds.components.*;
import com.fds.components.reports.*;
import com.fds.repositories.ReportContent;
import com.vaadin.ui.Component;

/**
 * Danh sach cac component duoc them vao tabsheet
 * Khi them moi cap nhat vao table FDS_DESCRIPTION de load vao menu, FDS_DESCRIPTION.DESCRIPTION can co noi dung nhu caption cua class
 * @see com.fds.views.MainView
 * */

public enum FdsTabType {

	INBOXS(Inbox.class,Inbox.CAPTION), 
	CASEDISTRIBUTION(CaseDistribution.class,CaseDistribution.CAPTION),
	CLOSECASE(ClosedCase.class,ClosedCase.CAPTION),
	EXCEPTIONCASE(ExceptionCase.class,ExceptionCase.CAPTION),
	SEARCH(Search.class,Search.CAPTION),
	FOLLOWUP(FollowUp.class,FollowUp.CAPTION),
//	PROCESSDIFFERENCE(ProcessDifference.class,ProcessDifference.CAPTION),
	SUPPLYBILL(SupplyBill.class,SupplyBill.CAPTION),
	ROLELIST(RoleList.class,RoleList.CAPTION),
	RULELIST(RuleList.class,RuleList.CAPTION),
	USERMANAGER(UserManager.class,UserManager.CAPTION),	
	UPDATETXNPOS(UploadTxnPos.class,UploadTxnPos.CAPTION),
	EXCEPTIONDVCNT(ExceptionDvcnt.class,ExceptionDvcnt.CAPTION),
	//Reports
	REPORTTOTALCASE(ReportTotalCase.class, ReportTotalCase.CAPTION),
	REPORTSTATICTISRULE(ReportStatisticRule.class, ReportStatisticRule.CAPTION),
	REPORTRULE(ReportRule.class, ReportRule.CAPTION),
	REPORTTID(ReportTID.class, ReportTID.CAPTION),
	REPORTMID(ReportMID.class, ReportMID.CAPTION),
	REPORTCASE(ReportCase.class, ReportCase.CAPTION),
	REPORTCARDTYPE(ReportCardType.class, ReportCardType.CAPTION),
	REPORTPAN(ReportPan.class, ReportPan.CAPTION),
	REPORTMCC(ReportMCC.class, ReportMCC.CAPTION),
	REPORTRESULTTXN(ReportResultTxn.class, ReportResultTxn.CAPTION),
	REPORTPOSMODE(ReportPosMode.class, ReportPosMode.CAPTION),
	REPORTCONTENT(ReportContent.class, ReportContent.CAPTION),
	REPORTTIME(ReportTime.class, ReportTime.CAPTION),
	REPORTSTATUSNOTIFYDVCNT(ReportStatusNofityDVCNT.class, ReportStatusNofityDVCNT.CAPTION),
	REPORTDS(ReportDS.class, ReportDS.CAPTION)
	/*REPORTCASEACTIONBYUSER(ReportCaseActionByUser.class,ReportCaseActionByUser.CAPTION),
	REPORTCASE(ReportCaseTotal.class,ReportCaseTotal.CAPTION),
	REPORTCASEBYTXN(ReportCaseByTxn.class,ReportCaseByTxn.CAPTION),
	REPORTCASEBYSTATUS(ReportCaseStatus.class,ReportCaseStatus.CAPTION),
	REPORTCASERULEID(ReportCaseRuleId.class,ReportCaseRuleId.CAPTION),
	REPORTCASETXNCRDDET(ReportCaseTxnCrdDet.class,ReportCaseTxnCrdDet.CAPTION)	*/
	
	
	;

	private final String caption;
	private final Class<? extends Component> classComponent;

	private FdsTabType(Class<? extends Component> classComponent,String caption) {
		this.caption = caption;
		this.classComponent = classComponent;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends Component> getClassComponent() {
		return classComponent;
	}	
	
	public static FdsTabType getTabType(final String caption){
		FdsTabType result=null;
		for (FdsTabType tabType:values()){
			if(tabType.getCaption().equals(caption)){
				result=tabType;
				break;
			}
		}
		return result;
	}
	

}

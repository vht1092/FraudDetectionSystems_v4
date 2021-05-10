package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import com.fds.entities.FdsPosSysTask;

public interface SysTaskService {
	Iterable<FdsPosSysTask> findAllByUseridWithCurrentTime(String userid, String type);

	FdsPosSysTask findOneByObjectTaskAndTypeTask(String object, String type);
	
	FdsPosSysTask findOneByMidOrTidAndTypeTask(String mid, String tid, String type);

	FdsPosSysTask findOneByObjectAndCurrentTime(String object, String type);
	
	FdsPosSysTask findOneByMidOrTidAndCurrentTime(String mid, String tid, String type);

	List<FdsPosSysTask> findAllByTypeTask(String typetask);

	void save(String object, String content, String type, String userid);

	void save(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid);

	void save(BigDecimal fromdate, BigDecimal todate, String content, String type, String userid, String createdate, String typeOfBusiness, String mid, String tid, FdsPosSysTask tempFdsSysTask);

	void save(FdsPosSysTask fdsPosSysTask);
	
	void update(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid);

	void delete(String userid, String object, String type);

	void delete(String userid, String type);

	void deleteByObjecttaskAndTypetask(String object, String type);
	
	void deleteByMidOrTidAndTypetask(String mid, String tid, String type);
	
	public List<Object[]> getUserUpdate(String mid, String tid);

}

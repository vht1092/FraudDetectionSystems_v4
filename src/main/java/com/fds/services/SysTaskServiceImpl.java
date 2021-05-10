package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fds.TimeConverter;
import com.fds.entities.FdsPosSysTask;
import com.fds.repositories.SysTaskRepo;

@Service("sysTaskService")
@Transactional
public class SysTaskServiceImpl implements SysTaskService {

	@Autowired
	private SysTaskRepo sysTaskRepo;
	private TimeConverter timeConverter;

	@Override
	public Iterable<FdsPosSysTask> findAllByUseridWithCurrentTime(String userid, String type) {
		return sysTaskRepo.findAllByUseridWithCurrentTime(userid, type);
	}

	@Override
	public void save(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid) {

		FdsPosSysTask fdsSysTask = new FdsPosSysTask();
		FdsPosSysTask tempFdsSysTask = sysTaskRepo.findOneByObjecttaskAndTypetask(object, type);
		if (tempFdsSysTask != null) {
			fdsSysTask = tempFdsSysTask;
		}
		fdsSysTask.setObjecttask(object);
		fdsSysTask.setFromdate(fromdate);
		fdsSysTask.setTodate(todate);
		fdsSysTask.setContenttask(content);
		fdsSysTask.setTypetask(type);
		fdsSysTask.setUserid(userid);
		sysTaskRepo.save(fdsSysTask);
	}

	@Override

	public void save(String object, String content, String type, String userid) {
		FdsPosSysTask fdsSysTask = new FdsPosSysTask();
		timeConverter = new TimeConverter();
		fdsSysTask.setObjecttask(object);
		fdsSysTask.setFromdate(new BigDecimal(timeConverter.getCurrentTime()));
		fdsSysTask.setTodate(new BigDecimal(timeConverter.getCurrentTime()));
		fdsSysTask.setContenttask(content);
		fdsSysTask.setTypetask(type);
		fdsSysTask.setUserid(userid);
		sysTaskRepo.save(fdsSysTask);
	}

	@Override
	public FdsPosSysTask findOneByObjectTaskAndTypeTask(String object, String type) {
		return sysTaskRepo.findOneByObjecttaskAndTypetask(object, type);
	}

	@Override
	public FdsPosSysTask findOneByObjectAndCurrentTime(String object, String type) {
		return sysTaskRepo.findOneByObjectAndCurrentTime(object, type);
	}

	@Override
	public void update(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid) {
		FdsPosSysTask fdsSysTask = sysTaskRepo.findOneByObjecttaskAndTypetask(object, type);
		fdsSysTask.setFromdate(fromdate);
		fdsSysTask.setTodate(todate);
		fdsSysTask.setContenttask(content);
		fdsSysTask.setUserid(userid);
		sysTaskRepo.save(fdsSysTask);

	}

	@Override
	public void delete(String userid, String object, String type) {
		sysTaskRepo.deleteByUseridAndObjecttaskAndTypetask(userid, object, type);
	}

	@Override
	public void delete(String userid, String type) {
		sysTaskRepo.deleteByUseridAndTypetask(userid, type);
	}

	@Override
	public List<FdsPosSysTask> findAllByTypeTask(String typetask) {
		return sysTaskRepo.findAllByTypetask(typetask);
	}

	@Override
	public void deleteByObjecttaskAndTypetask(String object, String type) {
		sysTaskRepo.deleteByObjecttaskAndTypetask(object, type);

	}

	@Override
	public void save(FdsPosSysTask fdsPosSysTask) {
		// TODO Auto-generated method stub
		sysTaskRepo.save(fdsPosSysTask);
	}

	@Override
	public void save(BigDecimal fromdate, BigDecimal todate, String content, String type, String userid, String createdate, String typeOfBusiness,
			String mid, String tid, FdsPosSysTask tempFdsSysTask) {
		// TODO Auto-generated method stub
		FdsPosSysTask fdsSysTask = new FdsPosSysTask();
		if (tempFdsSysTask != null) {
			fdsSysTask = tempFdsSysTask;
		}
//		fdsSysTask.setObjecttask("test");
		fdsSysTask.setFromdate(fromdate);
		fdsSysTask.setTodate(todate);
		fdsSysTask.setContenttask(content);
		fdsSysTask.setTypetask(type);
		fdsSysTask.setUserid(userid);
		fdsSysTask.setCreatedate(new BigDecimal(createdate));
		fdsSysTask.setTypeOfBusiness(typeOfBusiness);
		fdsSysTask.setMid(mid);
		fdsSysTask.setTid(tid);
		sysTaskRepo.save(fdsSysTask);
	}

	@Override
	public FdsPosSysTask findOneByMidOrTidAndCurrentTime(String mid, String tid, String type) {
		// TODO Auto-generated method stub
		return sysTaskRepo.findOneByMidOrTidAndCurrentTime(mid, tid, type);
	}

	@Override
	public List<Object[]> getUserUpdate(String mid, String tid) {
		// TODO Auto-generated method stub
		return sysTaskRepo.getUserUpdate(mid, tid);
	}

	@Override
	public FdsPosSysTask findOneByMidOrTidAndTypeTask(String mid, String tid, String type) {
		// TODO Auto-generated method stub
		return sysTaskRepo.findOneByMidOrTidAndTypetask(mid, tid, type);
	}

	@Override
	public void deleteByMidOrTidAndTypetask(String mid, String tid, String type) {
		// TODO Auto-generated method stub
		sysTaskRepo.deleteByMidAndTidAndTypetask(mid, tid, type);
	}

}

package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fds.entities.FdsPosSysTask;

@Transactional
@Repository
public interface SysTaskRepo extends CrudRepository<FdsPosSysTask, Long> {
	@Query(value = "select count(t.id) from FdsPosSysTask t where t.objecttask = :caseno and :currentdate between t.fromdate and t.todate")
	int countByCaseNo(@Param("caseno") String caseno, @Param("currentdate") BigDecimal currentdate);

	@Query(value = "select t.id,t.fromdate,t.todate,t.object_task,t.type_task,t.priority,t.userid  from fds_pos_sys_task t where t.userid = :userid and substr(t.todate, 0, 12) = to_char(to_date('201610311631','yyyyMMddHH24MI'), 'yyyyMMddHH24MI') and t.type_task = :type", nativeQuery = true)
	Iterable<FdsPosSysTask> findAllByUseridWithCurrentTime(@Param("userid") String userid, @Param("type") String type);

	@Query("select f from FdsPosSysTask f where f.objecttask=:object and to_number(to_char(sysdate, 'yyyyMMddHH24MISSSSS')) between f.fromdate and f.todate and f.typetask=:type")
	FdsPosSysTask findOneByObjectAndCurrentTime(@Param("object") String object, @Param("type") String type);

	@Query("select f from FdsPosSysTask f where (f.mid=:mid or f.tid=:tid) and to_number(to_char(sysdate, 'yyyyMMddHH24MISSSSS')) between f.fromdate and f.todate and f.typetask=:type")
	FdsPosSysTask findOneByMidOrTidAndCurrentTime(@Param("mid") String mid,@Param("tid") String tid, @Param("type") String type);

	
	@Query("select f from FdsPosSysTask f where f.objecttask=:object and f.typetask=:type")
	Iterable<FdsPosSysTask> findAllByObjectTask(@Param("object") String object, @Param("type") String type);

	List<FdsPosSysTask> findAllByTypetask(String type);

	// Dung cho exception case
	// @Query("select f from FdsSysTask f where f.objecttask=:object and
	// f.typetask=:type")
	FdsPosSysTask findOneByObjecttaskAndTypetask(@Param("object") String object, @Param("type") String type);

	@Query(value = "select to_number(to_char(SYSDATE, 'yyyyMMddHH24MISSSSS')) from dual", nativeQuery = true)
	BigDecimal getCurrentTime();

	@Query("select t from FdsPosSysTask t where not (:currentdate between t.fromdate and t.todate) and t.typetask=:type")
	Iterable<FdsPosSysTask> findAllByTypeAndNotInCurrenttime(@Param("currentdate") BigDecimal currentdate, @Param("type") String type);

	void deleteByUseridAndObjecttaskAndTypetask(@Param("userid") String userid, @Param("object") String object, @Param("type") String type);

	void deleteByUseridAndTypetask(@Param("userid") String userid, @Param("type") String type);

	void deleteByObjecttaskAndTypetask(@Param("object") String object, @Param("type") String type);
	
	@Modifying
	@Query("delete FdsPosSysTask t where (t.mid=:mid or t.tid=:tid) and t.typetask=:type")
	void deleteByMidAndTidAndTypetask(@Param("mid") String mid, @Param("tid") String tid, @Param("type") String type);
	
	@Query(value = "select USERID, CREATEDATE from {h-schema}FDS_POS_SYS_TASK where mid=:mid or tid=:tid", nativeQuery = true)
	public List<Object[]> getUserUpdate(@Param("mid") String mid,@Param("tid") String tid);

	@Query("select t from FdsPosSysTask t where (t.mid=:mid or t.tid=:tid) and t.typetask=:type")
	FdsPosSysTask findOneByMidOrTidAndTypetask(@Param("mid") String mid,@Param("tid") String tid, @Param("type") String type);
}

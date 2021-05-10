package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fds.entities.FdsPosDescription;

public interface DescriptionRepo extends CrudRepository<FdsPosDescription, Long> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM FDS_POS_DESCRIPTION WHERE TYPE = :type AND SEQUENCENO IS NOT NULL ORDER BY SEQUENCENO ASC")
	public List<FdsPosDescription> findAllByTypeByOrderBySequencenoAsc(@Param(value = "type") String type);
	
	Iterable<FdsPosDescription> findAllByType(String type);
}

package com.wonders.framework.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.auth.entity.Group;
import com.wonders.framework.repository.MyRepository;

public interface GroupRepository extends MyRepository<Group, Long> {
	@Query(" select distinct(g) from Group g left join fetch g.children where g.parent.id = ?1 ")
	List<Group> findByParentId(Long parentId);
	
	@Query(" select distinct(g) from Group g left join fetch g.children where g.parent.groupcode = ?1 order by g.ordernum ASC ")
	List<Group> findByParentGroupCod(String groupCode);
	
	@Query(" select distinct(g) from Group g left join fetch g.children where g.parent.groupcode = ?1 and g.groupcode in ?2 ")
	List<Group> findByParentGroupCodeFilter(String groupCode, List<String> filter);
}

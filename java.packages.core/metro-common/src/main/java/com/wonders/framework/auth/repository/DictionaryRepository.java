package com.wonders.framework.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.repository.MyRepository;

public interface DictionaryRepository extends MyRepository<Dictionary, Long> {
	@Query("select distinct(a) from Dictionary a left join fetch a.children c where a.parent.id = ?1 and a.typecode = ?2 order by a.ordernum asc,c.ordernum asc ")
	List<Dictionary> findByParentId(long parentId, String typecode);
	
	@Query("select distinct(a) from Dictionary a left join fetch a.children c where a.parent.id = ?1 order by a.ordernum asc,c.ordernum asc ")
	List<Dictionary> findByParentId(long parentId);
	
	@Query("select distinct(a) from Dictionary a left join fetch a.children where a.typecode = ?1 order by a.ordernum asc ")
	List<Dictionary> findByTypeCode(String typeCode);
	
	@Query("select distinct(a) from Dictionary a left join fetch a.children where a.typecode = ?1 and a.parent.code = ?2")
	List<Dictionary> findByParentCode(String typeCode,String parentCode);
	
	@Query("from Dictionary where code = ?1")
	List<Dictionary> validateDictionaryCode(String code);
	
	@Query("from Dictionary where code = ?1 and id <> ?2")
	List<Dictionary> validateDictionaryCode(String code, long id);
	
	List<Dictionary> findByTypecode(String typecode);
	
	@Query("select distinct(a) from Dictionary a where a.typecode = ?1 order by a.ordernum asc ")
	List<Dictionary> findByTypeCodeNoFetch(String typeCode);
	
	@Query("select distinct(a) from Dictionary a where a.parent.id = ?1 and a.typecode = ?2 order by a.ordernum asc ")
	List<Dictionary> findByParentIdNoFetch(long parentId, String typecode);
	
//	
////	@Query("select distinct(a) from Dictionary a left join fetch a.children c where a.typecode = ?1 and a.code not like '?2%' and c.code not like '?2%' order by a.ordernum asc ")
//	@Query("select distinct(a) from Dictionary a left join (select * from a.children b where b.code not like ?3) c on c.parent.id = a.id where a.parent.id = ?1 and a.typecode = ?2 order by a.ordernum asc,c.ordernum asc ")
//	List<Dictionary> findByTypeCodeParentIdUnlikeStr(long parentId,String typeCode,String UnLikecode);
	
}

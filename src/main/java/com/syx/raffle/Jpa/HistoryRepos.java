package com.syx.raffle.Jpa;


import com.syx.raffle.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HistoryRepos extends JpaRepository<History, Long> {
    List<History> findAll();

    History findByDate(Date date);



    @Query("select p.term from History p where p.term  in :termList")
    List<Integer> getExistTermList(@Param("termList") List<Integer> termList);


    @Query("select p from History p where p.date between :startDate and :endDate order by p.date asc")
    List<History> getRangeTerm(@Param("startDate")Date startDate,@Param("endDate")Date endDate);

}

package com.ccsw.tutorial.loan;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ccsw.tutorial.loan.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Page<Loan> findAll(Pageable pageable);

    @Query("select l from Loan l where (:game is null or l.game.id = :game) and (:client is null or l.client.id = :client) and (:date is null or (l.loanDate <= :date and :date <= l.returnDate))")
    Page<Loan> find(Pageable pageable, @Param("game") Long game, @Param("client") Long client,
            @Param("date") Date date);

    List<Loan> findByClientId(Long clientId);

    List<Loan> findByGameId(Long gameId);
}

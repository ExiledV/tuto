package com.ccsw.tutorial.loan;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.tutorial.config.mapper.BeanMapper;
import com.ccsw.tutorial.loan.exceptions.GameAlreadyBorrowedException;
import com.ccsw.tutorial.loan.exceptions.InvalidDateRangeException;
import com.ccsw.tutorial.loan.exceptions.MaxClientLoansReachedException;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

/**
 * @author Raúl Gómez Beteta
 */
@RequestMapping(value = "/loans")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    BeanMapper beanMapper;

    // @RequestMapping(path = "", method = RequestMethod.POST)
    // public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto) {
    // return this.beanMapper.mapPage(this.loanService.findPage(dto),
    // LoanDto.class);
    // }

    @SuppressWarnings("deprecation")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto,
            @RequestParam(value = "idGame", required = false) Long game,
            @RequestParam(value = "idClient", required = false) Long client,
            @RequestParam(value = "filterDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date date) {

        return this.beanMapper.mapPage(this.loanService.findPage(dto, game, client, date), LoanDto.class);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void save(@RequestBody LoanDto dto)
            throws GameAlreadyBorrowedException, MaxClientLoansReachedException, InvalidDateRangeException {
        this.loanService.save(dto);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.loanService.delete(id);
    }
}

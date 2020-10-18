package com.mamalimomen.services.Impl;

import com.mamalimomen.base.services.impl.BaseServiceImpl;
import com.mamalimomen.domains.Account;
import com.mamalimomen.domains.User;
import com.mamalimomen.dtos.AccountDTO;
import com.mamalimomen.dtos.AccountSearchDTO;
import com.mamalimomen.dtos.UserDTO;
import com.mamalimomen.repositories.AccountRepository;
import com.mamalimomen.repositories.impl.AccountRepositoryImpl;
import com.mamalimomen.services.AccountService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl extends BaseServiceImpl<Long, Account, AccountSearchDTO, AccountRepository> implements AccountService {
    public AccountServiceImpl(EntityManager em) {
        super(new AccountRepositoryImpl(em));
    }

    @Override
    public boolean createNewAccount(AccountDTO dto) {
        User user = new User();
        user.copyMeFrom(dto.getUser());

        Account account = new Account();
        account.setUser(user);

        return saveOne(account);
    }

    @Override
    public Optional<AccountDTO> retrieveExistActiveAccount(AccountDTO dto) {
        Optional<Account> oAccount = repository.findOneActiveAccountByUsername(dto.getUser().getUsername());

        if (oAccount.isPresent()) {
            Account account = oAccount.get();
            UserDTO userDTO = account.getUser().copyMeTo();
            dto.setId(account.getId());
            dto.setUser(userDTO);
        } else {
            return Optional.empty();
        }

        return Optional.of(dto);
    }

    @Override
    public boolean updateExistActiveAccount(AccountDTO dto) {
        User user = new User();
        user.copyMeFrom(dto.getUser());

        Account account = findOneById(Account.class,dto.getId()).get();
        //System.out.println(account.getId() + "" + account);
        account.setUser(user);
        //System.out.println(account.getId() + "" + account);

        return updateOne(account);
    }

    @Override
    public boolean deleteExistActiveAccount(AccountDTO dto) {
        Account account = findOneById(Account.class,dto.getId()).get();
        account.setDeleted(true);

        return updateOne(account);
    }

    @Override
    public List<AccountDTO> accountAdvancedSearch() {
        return null;
    }
}

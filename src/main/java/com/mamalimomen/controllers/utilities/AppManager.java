package com.mamalimomen.controllers.utilities;

import com.mamalimomen.base.controllers.utilities.PersistenceUnitManager;
import com.mamalimomen.base.controllers.utilities.PersistenceUnits;
import com.mamalimomen.base.domains.BaseEntity;
import com.mamalimomen.base.dtos.BaseDTO;
import com.mamalimomen.base.services.BaseService;
import com.mamalimomen.services.Impl.AccountServiceImpl;
import com.mamalimomen.services.Impl.PostServiceImpl;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppManager {
    private static final List<EntityManager> emList = new ArrayList<>();
    private static final Map<Services, BaseService<? extends Number, ? extends BaseEntity<? extends Number>, ? extends BaseDTO<? extends Number>>> serviceMapper = new HashMap<>();

    private AppManager() {
    }

    public static synchronized void startApp() {
        EntityManager em = PersistenceUnitManager.getEntityManager(PersistenceUnits.UNIT_ONE);

        emList.add(em);

        serviceMapper.put(Services.ACCOUNT_SERVICE, new AccountServiceImpl(em));
        serviceMapper.put(Services.POST_SERVICE, new PostServiceImpl(em));

        MenuFactory.getMenu(null).routerMenu();//FIXME
    }

    public static synchronized <PK extends Number, E extends BaseEntity<PK>, D extends BaseDTO<Long>, S extends BaseService<PK, E, D>> S getService(Services service) {
        return (S) serviceMapper.get(service);
    }

    public static synchronized void endApp() {
        for (EntityManager em : emList) {
            em.close();
        }

        PersistenceUnitManager.closeAllPersistenceProviders();
    }
}

package com.mamalimomen.base.controllers.utilities;

import com.mamalimomen.base.dtos.BaseDTO;
import com.mamalimomen.controllers.utilities.AppManager;
import com.mamalimomen.controllers.utilities.Services;
import com.mamalimomen.dtos.AccountDTO;
import com.mamalimomen.dtos.PostDTO;
import com.mamalimomen.services.AccountService;
import com.mamalimomen.services.PostService;

import java.util.Optional;

public final class MAMPParser {
    private final static AccountService accountService = AppManager.getService(Services.ACCOUNT_SERVICE);
    private final static PostService postService = AppManager.getService(Services.POST_SERVICE);

    private MAMPParser() {
    }

    public static Object parse(MAMP<? extends BaseDTO> request) {
        return switch (request.getCommand()) {
            case CREATE -> createRequestParser(request);
            case RETRIEVE -> retrieveRequestParser(request);
            case UPDATE -> updateRequestParser(request);
            case DELETE -> deleteRequestParser(request);
        };
    }

    private static String deleteRequestParser(MAMP<? extends BaseDTO> request) {
        if (request.getDtoClass().equals(AccountDTO.class)) {

            if (accountService.deleteExistActiveAccount((AccountDTO) request.getData())) {
                return "successful delete request";
            } else {
                return "unsuccessful delete request";
            }

        } else if (request.getDtoClass().equals(PostDTO.class)) {

            if (postService.deleteExistPost((PostDTO) request.getData())) {
                return "successful delete request";
            } else {
                return "unsuccessful delete request";
            }

        } else {
            return "unknown request dtoClass";
        }
    }

    private static String updateRequestParser(MAMP<? extends BaseDTO> request) {
        if (request.getDtoClass().equals(AccountDTO.class)) {

            if (accountService.updateExistActiveAccount((AccountDTO) request.getData())) {
                return "successful update request";
            } else {
                return "unsuccessful update request";
            }

        } else if (request.getDtoClass().equals(PostDTO.class)) {

            if (postService.updateExistPost((PostDTO) request.getData())) {
                return "successful update request";
            } else {
                return "unsuccessful update request";
            }

        } else {
            return "unknown request dtoClass";
        }
    }

    private static Object retrieveRequestParser(MAMP<? extends BaseDTO> request) {

        if (request.getDtoClass().equals(AccountDTO.class)) {

            if (request.getCount() == Count.ONE) {
                Optional<AccountDTO> oAccountDTO = accountService.retrieveExistActiveAccount((AccountDTO) request.getData());

                if (oAccountDTO.isPresent()) {
                    return oAccountDTO.get();
                } else {
                    return "unsuccessful retrieve request";
                }
            } else {
                return "unknown request count";
            }

        } else if (request.getDtoClass().equals(PostDTO.class)) {

            return switch (request.getCount()) {
                case ONE -> "unknown request count";
                case MANY -> postService.retrieveManyExistPosts((PostDTO) request.getData());
                case ALL -> postService.retrieveAllExistPosts();
            };

        } else {
            return "unknown request dtoClass";
        }
    }

    private static String createRequestParser(MAMP<? extends BaseDTO> request) {
        if (request.getDtoClass().equals(AccountDTO.class)) {

            if (accountService.createNewAccount((AccountDTO) request.getData())) {
                return "successful create request";
            } else {
                return "unsuccessful create request";
            }

        } else if (request.getDtoClass().equals(PostDTO.class)) {

            if (postService.createNewPost((PostDTO) request.getData())) {
                return "successful create request";
            } else {
                return "unsuccessful create request";
            }

        } else {
            return "unknown request dtoClass";
        }
    }
}

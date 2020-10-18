package com.mamalimomen.base.controllers.utilities;

import com.mamalimomen.base.dtos.BaseDTO;

import java.io.Serializable;

public final class MAMP<D extends BaseDTO> implements Serializable {

    private static final long serialVersionUID = -2846415079417991038L;

    private final Command command;
    private final Count count;
    private final Class<D> dtoClass;
    private final D data;

    public MAMP(Command command, Count count, D data) {
        this.command = command;
        this.count = count;
        this.dtoClass = (Class<D>) data.getClass();
        this.data = data;
    }

    public Command getCommand() {
        return command;
    }

    public Count getCount() {
        return count;
    }

    public Class<D> getDtoClass() {
        return dtoClass;
    }

    public D getData() {
        return data;
    }
}

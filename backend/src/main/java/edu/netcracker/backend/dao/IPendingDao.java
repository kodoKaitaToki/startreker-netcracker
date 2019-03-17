package edu.netcracker.backend.dao;

import java.util.List;

public interface IPendingDao<T> {
    List<T> getPendingEntries();

    List<T> getPendingWithOffsetAndLimit(Number limit, Number offset);
}

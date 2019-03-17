package edu.netcracker.backend.service;

import java.util.List;

public interface IPendingSrvc<T> {

    List<T> getPendingEntries();

    List<T> getPendingWithOffsetAndLimit(Number limit, Number offset);
}

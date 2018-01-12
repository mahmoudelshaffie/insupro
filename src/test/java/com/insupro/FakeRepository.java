package com.insupro;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

public class FakeRepository<T, ID extends Serializable> implements CrudRepository<T, ID> {

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(ID arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(T arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Iterable<? extends T> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(ID arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<T> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<T> findAll(Iterable<ID> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T findOne(ID arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends T> S save(S arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> arg0) {
		throw new UnsupportedOperationException();
	}

}

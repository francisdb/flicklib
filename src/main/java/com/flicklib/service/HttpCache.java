package com.flicklib.service;

public interface HttpCache {

	public abstract Source get(String url);

	public abstract void put(String url, Source page);

}
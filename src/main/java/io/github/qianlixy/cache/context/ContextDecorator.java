package io.github.qianlixy.cache.context;

public abstract class ContextDecorator implements Context {
	
	protected Context context;
	
	public ContextDecorator(Context context) {
		this.context = context;
	}
	
	@Override
	public void setAttribute(Object key, Object value) {
		context.setAttribute(key, value);
	}

	@Override
	public Object getAttribute(Object key) {
		return context.getAttribute(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAutoAttribute(Object key) {
		return (T) getAttribute(key);
	}

	@Override
	public void removeAttribute(Object key) {
		context.removeAttribute(key);
	}
	
	@Override
	public void clear() {
		context.clear();
	}

	@Override
	public boolean isValid() {
		return context.isValid();
	}

}

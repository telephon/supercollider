+ LazyEnvir{
	put { arg key, obj;
		if(obj.isFunction){obj=obj.inEnvir(this.envir)};
		envir.put(key, obj);
		dispatch.value(key, obj);
	}
}
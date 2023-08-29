package modules;

import com.google.inject.AbstractModule;

import service.GameService;

public class MyModule extends AbstractModule {
    
    @Override
    protected void configure() {
        bind(GameService.class);
    }

}

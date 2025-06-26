package org.spdgrupo.elbuensaborapi.config.startup;

import org.spdgrupo.elbuensaborapi.service.RolService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolSyncOnStartup implements ApplicationRunner {

    private final RolService rolService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        rolService.sincronizarRolesDesdeAuth0();
    }
}
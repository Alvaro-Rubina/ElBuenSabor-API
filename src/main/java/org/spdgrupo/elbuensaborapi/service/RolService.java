package org.spdgrupo.elbuensaborapi.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.Role;
import com.auth0.json.mgmt.RolesPage;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RolMapper;
import org.spdgrupo.elbuensaborapi.model.dto.rol.RolDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rol.RolResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Rol;
import org.spdgrupo.elbuensaborapi.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;
    private final ManagementAPI managementAPI;

    @Transactional
    public void save(RolDTO rolDTO) throws Auth0Exception {
        Rol rol = rolMapper.toEntity(rolDTO);

        // auth0
        Role rolAuth0 = new Role();
        rolAuth0.setName(rolDTO.getNombre());
        rolAuth0.setDescription(rolDTO.getDescripcion());
        rolAuth0 = managementAPI.roles().create(rolAuth0).execute();

        rol.setAuth0RolId(rolAuth0.getId());

        try {
            rolRepository.save(rol);
        } catch (Exception e) {
            // Si falla en la BD, elimino el rol en Auth0
            managementAPI.roles().delete(rolAuth0.getId()).execute();
            throw e;
        }
    }

    public RolResponseDTO findById(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol con el id " + id + " no encontrado"));
        return rolMapper.toResponseDTO(rol);
    }

    public List<RolResponseDTO> findAll() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toResponseDTO)
                .toList();
    }

    public RolResponseDTO findByAuth0RolId(String id) {
        Rol rol = rolRepository.findByAuth0RolId(id)
                .orElseThrow(() -> new NotFoundException("Rol con el id " + id + " no encontrado"));
        return rolMapper.toResponseDTO(rol);
    }


    public RolResponseDTO findByNombre(String nombre){
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new NotFoundException("Rol con el nombre " + nombre + " no encontrado"));
        return rolMapper.toResponseDTO(rol);
    }

    @Transactional
    public void update(String auth0RolId, RolDTO rolDTO) throws Auth0Exception {
        Rol rol = rolRepository.findByAuth0RolId(auth0RolId)
                .orElseThrow(() -> new NotFoundException("Rol con el auht0Id " + auth0RolId + " no encontrado"));

        // Guardar estado anterior de Auth0
        Role previousAuth0Role = managementAPI.roles().get(auth0RolId).execute();

        Role rolAuth0 = new Role();
        boolean changed = false;

        if ((!rol.getNombre().equals(rolDTO.getNombre())) && (!rolDTO.getNombre().trim().isEmpty())) {
            rol.setNombre(rolDTO.getNombre());
            rolAuth0.setName(rolDTO.getNombre());
            changed = true;
        }

        if ((!rol.getDescripcion().equals(rolDTO.getDescripcion())) && (!rolDTO.getDescripcion().trim().isEmpty())) {
            rol.setDescripcion(rolDTO.getDescripcion());
            rolAuth0.setDescription(rolDTO.getDescripcion());
            changed = true;
        }

        if (changed) {
            try {
                // Actualizo en auth0 y en la bd
                managementAPI.roles().update(auth0RolId, rolAuth0).execute();
                rolRepository.save(rol);
            } catch (Exception e) {
                // Revierto en Auth0 si falla la base de datos
                Role revertRole = new Role();
                revertRole.setName(previousAuth0Role.getName());
                revertRole.setDescription(previousAuth0Role.getDescription());
                managementAPI.roles().update(auth0RolId, revertRole).execute();
                throw e;
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol con el id " + id + " no encontrado"));
        rol.setActivo(false);
        rolRepository.save(rol);
    }

    @Transactional
    public void deleteByAuth0RolId(String id) {
        Rol rol = rolRepository.findByAuth0RolId(id)
                .orElseThrow(() -> new NotFoundException("Rol con el auht0Id " + id + " no encontrado"));
        rol.setActivo(false);
        rolRepository.save(rol);
    }

    @Transactional
    public void deletePhysicallyByAuth0RolId(String id) throws Auth0Exception {
        Rol rol = rolRepository.findByAuth0RolId(id)
                .orElseThrow(() -> new NotFoundException("Rol con el auht0Id " + id + " no encontrado"));
        managementAPI.roles().delete(id).execute();
        rolRepository.delete(rol);
    }

    public void toggleActivo(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol con el id " + id + " no encontrado"));
        rol.setActivo(!rol.getActivo());
        rolRepository.save(rol);
    }

    @Transactional
    public void sincronizarRolesDesdeAuth0() throws Auth0Exception {
        RolesPage rolesPage = managementAPI.roles().list(null).execute();
        List<Role> rolesAuth0 = rolesPage.getItems();

        for (Role roleAuth0 : rolesAuth0) {
            if (!rolRepository.existsByAuth0RolId(roleAuth0.getId())) {
                Rol rol = new Rol();
                rol.setNombre(roleAuth0.getName());
                rol.setDescripcion(roleAuth0.getDescription());
                rol.setAuth0RolId(roleAuth0.getId());
                rol.setActivo(true);
                rolRepository.save(rol);
            }
        }
    }

    public boolean existsByAuth0RolId(String auth0RolId) {
        return rolRepository.existsByAuth0RolId(auth0RolId);
    }

    /*@Transactional
    public Page<Rol> findAll(Pageable pageable) throws Exception {
        try {
            Page<Rol> entities = rolRepository.findAll(pageable);
            return entities;
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }*/
}
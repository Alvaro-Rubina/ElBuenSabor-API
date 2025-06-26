package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DomicilioService extends GenericoServiceImpl<Domicilio, DomicilioDTO, DomicilioResponseDTO, Long> {

    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private DetalleDomicilioRepository detalleDomicilioRepository;
    @Autowired
    private DomicilioMapper domicilioMapper;

    public DomicilioService(GenericoRepository<Domicilio, Long> genericoRepository, GenericoMapper<Domicilio,DomicilioDTO,DomicilioResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Cacheable(value = "domicilios", key = "'getDomiciliosByClienteId_'+#id")
    public List<DomicilioResponseDTO> getDomiciliosByClienteId(Long clienteId){
        List<DetalleDomicilio> detalleDomicilios = detalleDomicilioRepository.findByClienteId(clienteId);
        return detalleDomicilios.stream()
                .map(detalleDomicilio -> domicilioMapper.toResponseDTO(detalleDomicilio.getDomicilio()))
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domicilio con el id " + id + " no encontrado"));
        domicilio.setActivo(false);
        domicilioRepository.save(domicilio);
    }

    @Override
    @CachePut(value = "domicilios", key = "'update_'+#id")
    @Transactional
    public void update(Long id, DomicilioDTO domicilioDTO) {
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domicilio no encontrado"));

        // Actualizamos todos los campos
        domicilio.setCalle(domicilioDTO.getCalle());
        domicilio.setLocalidad(domicilioDTO.getLocalidad());
        domicilio.setNumero(domicilioDTO.getNumero());
        domicilio.setCodigoPostal(domicilioDTO.getCodigoPostal());
        domicilio.setActivo(true);

        domicilioRepository.save(domicilio);
    }

    @Override
    @Cacheable("domicilios")
    public List<DomicilioResponseDTO> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "domicilios", key = "'findById_'+#id")
    public DomicilioResponseDTO findById(Long id) {
        return super.findById(id);
    }

    @Override
    @CacheEvict(value = "domicilios", allEntries = true)
    @Transactional
    public DomicilioResponseDTO save(DomicilioDTO dto) {
        return super.save(dto);
    }

    @Override
    @CacheEvict(value = "domicilios", key = "'toggleActivo_'+#id")
    @Transactional
    public void toggleActivo(Long id) {
        super.toggleActivo(id);
    }
}

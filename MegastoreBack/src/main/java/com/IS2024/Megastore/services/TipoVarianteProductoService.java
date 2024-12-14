/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.TipoVarianteProducto;
import com.IS2024.Megastore.repositories.TipoVarianteProductoRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

/**
 *
 * @author maite
 */
@Service
public class TipoVarianteProductoService implements TipoVarianteProductoRepository {

    
    @Autowired
    private TipoVarianteProductoRepository repository;

    
    @Override
    public Optional<TipoVarianteProducto> findById(Long id) {
        return this.repository.findById(id);
    }
    
    @Override
    public TipoVarianteProducto getById(Long id) {
        return this.repository.getById(id);
    }
    
    @Override
    public List<TipoVarianteProducto> findAll() {
        return this.repository.findAll();
    }
    
    public TipoVarianteProducto updateTipoVarianteProducto(Long id, TipoVarianteProducto tipoVarianteProducto){
        Optional<TipoVarianteProducto> existingTipoVarianteProducto = this.repository.findById(id);
        if(existingTipoVarianteProducto.isPresent()){
            TipoVarianteProducto updatedTipoVarianteProducto = existingTipoVarianteProducto.get();
            updatedTipoVarianteProducto.setNombre(tipoVarianteProducto.getNombre());
            Optional<TipoVarianteProducto> existTipo = this.repository.findByCodigo(tipoVarianteProducto.getCodigo());
            if(existTipo.isPresent()){
                throw new InvalidEntityException("Ya existe un tipo variante con el mismo codigo");
            } else {
                updatedTipoVarianteProducto.setCodigo(tipoVarianteProducto.getCodigo());
            }
            return this.repository.save(updatedTipoVarianteProducto);
        } else {
            throw new ResourceNotFoundException("TipoVarianteProducto no encontrado con id: " + id);
        }
    }
    
    public TipoVarianteProducto createTipoVarianteProducto(TipoVarianteProducto tipoVarianteProducto){
        if(!tipoVarianteProducto.getCodigo().isEmpty()){
            Optional<TipoVarianteProducto> existTipo = this.repository.findByCodigo(tipoVarianteProducto.getCodigo());
            if(existTipo.isPresent()){
                throw new InvalidEntityException("Ya existe un tipo variante con el mismo codigo");
            } else {
               return this.repository.save(tipoVarianteProducto);
            }
        } else {
            throw new InvalidEntityException("El codigo es un campo obligatorio");
        }
         
        
    }

    @Override
    public void deleteById(Long id) {
        if(this.repository.existsById(id)){
            this.repository.deleteById(id);
        }else {
            throw new ResourceNotFoundException("TipoVarianteProducto no encontrado con id: " + id);
        }
    }
    
    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAllInBatch(Iterable<TipoVarianteProducto> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public TipoVarianteProducto getOne(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public TipoVarianteProducto getReferenceById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<TipoVarianteProducto> findAllById(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> S save(S entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(TipoVarianteProducto entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll(Iterable<? extends TipoVarianteProducto> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<TipoVarianteProducto> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<TipoVarianteProducto> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> long count(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <S extends TipoVarianteProducto, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<TipoVarianteProducto> findByCodigo(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.VarianteProducto;
import com.IS2024.Megastore.entities.TipoVarianteProducto;
import com.IS2024.Megastore.repositories.VarianteProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VarianteProductoService {

    @Autowired
    private VarianteProdutoRepository repository;

    @Autowired
    private TipoVarianteProductoService tipoVarianteService;

    // Métodos del servicio que usan el repositorio
    public List<VarianteProducto> findByNombre(String nombre) {
        return this.repository.findByNombre(nombre);
    }

    public Optional<VarianteProducto> findById(Long id) {
        return this.repository.findById(id);
    }

    @SuppressWarnings("deprecation")
    public VarianteProducto getById(Long id) {
        return this.repository.getById(id);
    }

    public List<VarianteProducto> findAll() {
        return this.repository.findAll();
    }

    public void deleteById(Long id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("VarianteProducto no encontrado con id: " + id);
        }
    }

    public VarianteProducto updateVarianteProducto(Long id, VarianteProducto varianteProducto) {
        Optional<VarianteProducto> existingVarianteProducto = this.repository.findById(id);
        if (existingVarianteProducto.isPresent()) {
            VarianteProducto updatedVarianteProducto = existingVarianteProducto.get();

            updatedVarianteProducto.setNombre(varianteProducto.getNombre());
            Optional<TipoVarianteProducto> tipoVariante = this.tipoVarianteService
                    .findById(varianteProducto.getTipoVariante().getId());
            if (tipoVariante.isPresent()) {
                updatedVarianteProducto.setTipoVariante(tipoVariante.get());
            } else {
                throw new ResourceNotFoundException(
                        "No existe un tipoVariante con id: " + varianteProducto.getTipoVariante().getId());
            }

            return this.repository.save(updatedVarianteProducto);
        } else {
            throw new ResourceNotFoundException("VarianteProducto no encontrado con id: " + id);
        }
    }

    public VarianteProducto createVarianteProducto(VarianteProducto varianteProducto) {
        Optional<TipoVarianteProducto> tipoVariante = this.tipoVarianteService
                .findById(varianteProducto.getTipoVariante().getId());
        if (tipoVariante.isPresent()) {
            return this.repository.save(varianteProducto);
        } else {
            throw new ResourceNotFoundException(
                    "No existe un tipoVariante con id: " + varianteProducto.getTipoVariante().getId());
        }
    }

    public List<VarianteProducto> findByTipoVarianteCodigo(String codigo) {
        return this.repository.findByTipoVarianteCodigo(codigo);
    }

}

package com.IS2024.Megastore.repositories;

import com.IS2024.Megastore.entities.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VarianteProdutoRepository extends JpaRepository<VarianteProducto, Long> {
    List<VarianteProducto> findByNombre(String nombre);

    List<VarianteProducto> findByTipoVarianteCodigo(String codigo);

}

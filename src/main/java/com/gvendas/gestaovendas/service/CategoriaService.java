package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.CategoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired  //Instancia em tempo de execução - sem ele pode dar NullPointer Exception
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {

        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria save(Categoria categoria) {
        validarCategoriaDuplicada(categoria);
        return categoriaRepository.save(categoria);
    }

    public Categoria update(Long id, Categoria categoria) {
        //Valida se pode fazer o update (nesse caso, se existe a categoria do parametro)
        Categoria categoriaSalvar = validarCategoriaExiste(id);
        validarCategoriaDuplicada(categoria);
        //atualiza a categoria do banco pela do parâmetro - exceto o ultimo campo, nesse caso o id
        BeanUtils.copyProperties(categoria, categoriaSalvar, "id");
        //salva as alterações
        return categoriaRepository.save(categoriaSalvar);
    }

    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }

    private Categoria validarCategoriaExiste(Long id) {
        Optional<Categoria> categoria = findById(id);
        if (categoria.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return categoria.get();
    }

    private void validarCategoriaDuplicada(Categoria categoria) {
        Categoria categoriaEncontrada = categoriaRepository.findByNome(categoria.getNome());
        if (categoriaEncontrada != null && categoriaEncontrada.getCodigo() != categoria.getCodigo()) {
            throw new RegraNegocioException(String.format("A categoria %s já está cadastrada.", categoria.getNome().toUpperCase()));
        }
    }
}

package com.example.mapleproject.services;

import com.example.mapleproject.entities.Empresa;
import com.example.mapleproject.entities.MovimientoDinero;
import com.example.mapleproject.repository.IEmpresaRepository;
import com.example.mapleproject.repository.IMovimientoDineroRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MovimientoDineroService {
    private IMovimientoDineroRepository movimientoDineroRepository;
    private IEmpresaRepository empresaRepository;

    public MovimientoDineroService(IMovimientoDineroRepository repository, IEmpresaRepository erepository) {
        this.movimientoDineroRepository = repository;
        this.empresaRepository = erepository;
    }

    public MovimientoDinero selectById(int Id) {
        Optional<MovimientoDinero> validaSiExisteId = this.movimientoDineroRepository.findById(Id);
        if (validaSiExisteId.isPresent()) {
            return validaSiExisteId.get();
        } else {
            return null;
        }
    }

    public ArrayList<MovimientoDinero> selectAll(int empresaId) {
        return (ArrayList<MovimientoDinero>) movimientoDineroRepository.findByEmpresaId(empresaId);
    }

    //El sistema permite consultar todos los movimientos
    public ArrayList<MovimientoDinero> selectAll() {
        return (ArrayList<MovimientoDinero>) this.movimientoDineroRepository.findAll();
    }

    public Response crearMovimientoDineroByEmpresa(MovimientoDinero data) {
        Response response = new Response();
        this.movimientoDineroRepository.save(data);
        response.setCode(200);
        response.setMessage("Movimiento registrado exitosamente");
        return response;
    }

    public Response crearMovimientoDinero(MovimientoDinero data) {
        Response response = new Response();
        this.movimientoDineroRepository.save(data);
        response.setCode(200);
        response.setMessage("Movimiento registrado exitosamente");
        return response;
    }



    public Response updateMovimientoDineroByEmpresa(MovimientoDinero data, int id) {
        Response response = new Response();

        if (id == 0) {
            response.setCode(500);
            response.setMessage("Error el ID de la empresa no es valido");
            return response;
        }

        //Si la empresa no existe, arroja error
        ArrayList<MovimientoDinero> movimientoEmpresa = selectAll(id);
        if (movimientoEmpresa == null) {
            response.setCode(500);
            response.setMessage("Error, no existen movimientos para la empresa ingresada");
            return response;
        }

        if (data.getId() == 0) {
            response.setCode(500);
            response.setMessage("Error el ID de la empresa no es valido");
            return response;
        }

        MovimientoDinero existsMovimiento = selectById(data.getId());
        if (existsMovimiento == null) {
            response.setCode(500);
            response.setMessage("Error, no existen movimientos para la empresa ingresada");
            return response;
        }

        boolean needUpdateMovimiento = false;

        if (data.getMonto() > 0) {
            existsMovimiento.setMonto(data.getMonto());
            needUpdateMovimiento = true;
        }

        if (StringUtils.hasLength(data.getConcepto())) {
            existsMovimiento.setConcepto(data.getConcepto());
            needUpdateMovimiento = true;
        }

        if (needUpdateMovimiento) {
            this.movimientoDineroRepository.save(existsMovimiento);
        }

            //Envia mensaje de actualización exitosa

            response.setCode(200);
            response.setMessage("Movimientos modificados exitosamente");
            return response;
        }

        public Response deleteMovimientoDinero (Empresa empresa){
            Response response = new Response();
            try {
                this.movimientoDineroRepository.deleteMovimientoDineroByEmpresa(empresa);
                response.setCode(200);
                response.setMessage("Movimiento de dinero eliminado exitosamente");
                return response;
            } catch (Exception ex) {
                response.setCode(500);
                response.setMessage("Error " + ex.getMessage());
                return response;
            }
        }

    //Servicio para ver la suma de todos los montos
    public Long obtenerSumaMontos(){
        return movimientoDineroRepository.SumarMonto();
    }

    //Servicio para ver la suma de los montos por empleado
    public Long MontosPorEmpleado(Integer id){
        return movimientoDineroRepository.MontosPorEmpleado(id);
    }

    //Servicio para ver la suma de los montos por empresa
    public Long MontosPorEmpresa(Integer id){
        return movimientoDineroRepository.MontosPorEmpresa(id);
    }

    //servicio que nos deja conseguir el id de un empleado si tenemos su correo
    public Integer IdPorCorreo(String Correo){
        return movimientoDineroRepository.IdPorCorreo(Correo);
    }
}

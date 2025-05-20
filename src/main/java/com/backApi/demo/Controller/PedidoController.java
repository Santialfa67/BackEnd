package com.backApi.demo.Controller;

import com.backApi.demo.Dto.PedidoDTO;
import com.backApi.demo.Model.Pedido;
import com.backApi.demo.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        List<Pedido> pedidos = pedidoService.getAllPedidos();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        return pedido.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido savedPedido = pedidoService.createPedido(pedidoDTO);
            return new ResponseEntity<>(savedPedido, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Integer id, @RequestBody Pedido pedido) {
        Optional<Pedido> existingPedido = pedidoService.getPedidoById(id);
        if (existingPedido.isPresent()) {
            pedido.setPedidoId(id); // Ensure the ID is set for updating
            Pedido updatedPedido = pedidoService.savePedido(pedido);
            return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Integer id) {
        if (pedidoService.existsById(id)) {
            pedidoService.deletePedido(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
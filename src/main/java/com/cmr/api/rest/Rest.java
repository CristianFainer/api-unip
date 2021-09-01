package com.cmr.api.rest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class Rest {

    // ROTA POST QUE RECEBE OS NUMEROS E A OPERAÇÃO COMO PARAMETRO NA ROTA E EXECUTA A CONTA
    @PostMapping(path = "/calculadora", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity calculadora(@RequestParam("numero1") String numero1, @RequestParam("numero2") String numero2, @RequestParam("numero3") String numero3, @RequestParam("operador") String operador) {
        JSONObject json = new JSONObject();
        double num1 = Double.parseDouble(numero1);
        double num2 = Double.parseDouble(numero2);
        double num3 = Double.parseDouble(numero3);
        String resultadoOp = "";
        if (operador.equals("soma")) operador = "+";
        if (operador.equals("sub")) operador = "-";
        if (operador.equals("mult")) operador = "*";
        if (operador.equals("div")) operador = "/";
        if (operador.equals("raiz")) operador = "raiz";
        if (operador.equals("potencia")) operador = "potencia";
        if (operador.equals("media arit")) operador = "media arit";
        if (operador.equals("media harm")) operador = "media harm";
        if (operador.equals("moda")) operador = "moda";

        if (isOperador(operador)) {
            double resultado = 0.0;
            switch (operador) {
                case "+":
                    resultado = num1 + num2;
                    break;
                case "-":
                    resultado = num1 - num2;
                    break;
                case "/":
                    resultado = num1 / num2;
                    break;
                case "*":
                    resultado = num1 * num2;
                    break;
                case "raiz":
                    resultadoOp = "Número 1: " + Math.sqrt(num1) + " | Número 2: " + Math.sqrt(num2);
                    break;
                case "potencia":
                    resultado = Math.pow(num1, num2);
                    break;
                case "media arit":
                    resultado = (num1 + num2) / 2;
                    break;
                case "media harm":
                    resultado = 2 / (1 / num1 + 1 / num2);
                    break;
                case "moda":
                    List<Integer> numberList = new ArrayList<>();
                    numberList.add(Integer.parseInt(String.valueOf(num1).replace(".0","")));
                    numberList.add(Integer.parseInt(String.valueOf(num2).replace(".0","")));
                    numberList.add(Integer.parseInt(String.valueOf(num3).replace(".0","")));
                    resultadoOp = moda(numberList).toString();
                    break;
            }

            json.put("Primeiro número", num1);
            json.put("Segundo número", num2);
            json.put("Operador", operador);

            if (operador.contains("raiz") || operador.contains("moda")) {
                json.put("Resultado", resultadoOp);
            } else {
                json.put("Resultado", resultado);
            }

            json.put("Mensagem", "Dados Retornados");
        } else {
            json.put("Mensagem", "Operador inválido");
            json.put("Resultado", "-");
        }

        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }

    // FUNÇÃO QUE VALIDA O OPERADOR
    private boolean isOperador(String caracter) {
        return caracter.equals("-") || caracter.equals("+")
                || caracter.equals("/") || caracter.equals("*")
                || caracter.equals("raiz") || caracter.equals("potencia")
                || caracter.equals("media arit") || caracter.equals("media harm")
                || caracter.equals("moda");
    }

    // ROTA POST QUE RECEBE OS NUMEROS COMO PARAMETRO NA ROTA E EXECUTA A SEQUÊNCIA FIBONACCI
    @PostMapping(path = "/fibonacci", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity fibonacci(@RequestParam("numero1") String numero1, @RequestParam("numero2") String numero2) {
        List<Long> listaFibonacci = new ArrayList<>();
        JSONObject json = new JSONObject();
        for (int i = Integer.parseInt(numero1); i < Integer.parseInt(numero2); i++) {
            listaFibonacci.add(fibo(i));
        }
        json.put("Fibonacci", listaFibonacci);
        json.put("Mensagem", "Dados Retornados");
        return new ResponseEntity(json.toString(), HttpStatus.OK);
    }

    // FUNÇÃO QUE PROCESSA A SEQUÊNCIA FIBONACCI
    public long fibo(int numero) {
        if (numero < 2) {
            return numero;
        } else {
            return fibo(numero - 1) + fibo(numero - 2);
        }
    }

    // FUNÇÃO CALCULA MODA
    public static List<Integer> moda(final List<Integer> numbers) {
        final Map<Integer, Long> countFrequencies = numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        final long maxFrequency = countFrequencies.values().stream()
                .mapToLong(count -> count)
                .max().orElse(-1);

        return countFrequencies.entrySet().stream()
                .filter(Tuple -> Tuple.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ordenador;

import ENEM.Aluno;
import java.nio.ByteBuffer;

/**
 *
 * @author Jocelino Neto
 */
public interface IBrentOrganizer {
    
    /**
     * TODO - Função HASH que calcula posição.
     * @param matricula
     * @return 
     */
    public long getHash(long matricula);
    
    /**
     * TODO - Função que calcula quantidade de incrementos em posição.
     * @param matricula
     * @return
     */
    public long getIncremento(long matricula);
    
    /**
     * TODO - Este método irá verificar a nova posição de um registro caso aja
     * choque em uma posição.
     * @param posicaoAtual
     * @param qtdSaltos
     * @param incremento
     * @return 
     */
    public long contarSaltos(long posicaoAtual, long qtdSaltos, long incremento);
    
    /**
     * TODO - Este metodo troca a posição de um registro e retorna um ByteBuffer
     * caso exista um registro na posição de troca.
     * @param aluno
     * @param posicaoAtual
     * @return 
     */
    public ByteBuffer trocarRegistro(Aluno aluno, long posicaoAtual);
    
    
    public boolean armazenamentoVazio(ByteBuffer bb, long posicao);
    
}

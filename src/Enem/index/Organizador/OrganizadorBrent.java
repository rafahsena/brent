package Organizador;
import ENEM.Aluno;
import Ferramentas.Conversor;
import Ordenador.IBrentOrganizer;
import Ordenador.IFileOrganizer;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizadorBrent implements IFileOrganizer, IBrentOrganizer{
    private FileChannel canal;
    private long tamanhoRegistro = Aluno.tamanho;
    private long tamanhoTabela = ((long)120000017);

    public OrganizadorBrent() {
    }

    public OrganizadorBrent(String nome) throws IOException {
        File arquivo = new File(nome);
        String permissao = "rw";
        RandomAccessFile ramFile = new RandomAccessFile(arquivo, permissao);
        this.canal = ramFile.getChannel();
    }

    
    @Override
    public void addAluno(Aluno aluno) {
        long atualPos = 0;
        boolean aqui = false;
        Aluno alunoTmp;
        int tamReg = (int) tamanhoRegistro;
        ByteBuffer bbtmp = ByteBuffer.allocate(tamReg);

        try {
            long tamanhoCanal = this.canal.size();
            if(tamanhoCanal == 0){
                this.canal.write(Conversor.toByteBuffer(aluno));
            }
            else{
                long posicaoReg = aluno.getMatricula();
                do{
                    this.canal.read(bbtmp, atualPos);
                    posicaoReg = this.getHash(posicaoReg);
                    
                    if(this.armazenamentoVazio(bbtmp, posicaoReg)){
                        this.canal.write(bbtmp, posicaoReg);
                        
                    }else{
                        bbtmp.position(0);
                        posicaoReg = this.getIncremeto(posicaoReg);
                        atualPos = atualPos +(tamReg * posicaoReg);
                    }
                   
                }while (tamanhoCanal > atualPos && !aqui);
               
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao adicionar o aluno");
            e.printStackTrace();
        }
        

}

    @Override
    public Aluno getAluno(long matricula){
        Aluno a = null; //new Aluno();
        long posicao;
          try {
            posicao = this.getPosition(matricula);
            //long tamanho = this.canal.size();
            if(posicao >= 0){
                // matricula, nome,       rua,        email,     curso
                // long        string     string      string     short
                //  8                                              2
                ByteBuffer bb = ByteBuffer.allocate((int) tamanhoRegistro);
                this.canal.read(bb, posicao);
                bb.position(0); //this.canal.write(bb, 0);   
                a = Conversor.toAluno(bb);
            }
          } catch (IOException ex) {
              System.err.println("Erro ao pegar matricula.");
          }
        return a;
    }
    

    @Override
    public Aluno delAluno(long matricula){
        Aluno aluno = null;
        try{
        long position = this.getPosition(matricula);
        if(position >= 0){
            long tam = this.canal.size();
            aluno = this.getAluno(matricula);
            ByteBuffer bb = ByteBuffer.allocate((int) tamanhoRegistro);
            for(long pos = position + tamanhoRegistro; pos < tam; pos += tamanhoRegistro){
                this.canal.position(pos);
                this.canal.read(bb);
                bb.flip();
                this.canal.write(bb, pos - tamanhoRegistro);
                bb.clear();
            }
            this.canal.truncate(tam - tamanhoRegistro);
            
        }
        }catch(Exception ex){
            System.err.println("Erro ao deletar aluno");
        }
        
        return aluno;
    }

    public long getPosition(long matricula) throws IOException {
        int tamanhoMatricula = 8;
        
        
        ByteBuffer bb = ByteBuffer.allocate(tamanhoMatricula);
        
        long posicao = -1;
        int contador;
        int repeticoes = (int) (this.canal.size() / tamanhoRegistro);
        
        for(contador=0; contador < repeticoes; contador++){
            this.canal.read(bb, contador*tamanhoRegistro);
            bb.position(0);
            
            if( matricula == bb.getLong() ){
                posicao = contador;
                contador = repeticoes;
            }
            
            bb.clear();
            
        }
        
        return posicao*tamanhoRegistro;
    }

    @Override
    public long getHash(long matricula) {
        return ((matricula)%tamanhoTabela);
    }

    @Override
    public long getIncremeto(long matricula) {
        return this.getHash((long)(matricula/tamanhoTabela));
    }

    @Override
    public long contarSaltos(long posicaoAtual, long qtdSaltos, long incremento) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ByteBuffer trocarRegistro(Aluno aluno, long posicaoAtual) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean armazenamentoVazio(ByteBuffer bb, long posicao) {
        bb.position((int)posicao);
        return bb.hasRemaining();
    }

}
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

public class OrganizadorBrent implements IFileOrganizer, IBrentOrganizer{
    private FileChannel canal;
    private long tamanhoRegistro = Aluno.tamanho;
    private long tamanhoTabela = (long)11;//((long)120000017);

    public OrganizadorBrent() {
    }

    public OrganizadorBrent(String nome) throws IOException {
        File arquivo = new File(nome);
        String permissao = "rw";
        RandomAccessFile ramFile = new RandomAccessFile(arquivo, permissao);
        this.canal = ramFile.getChannel();
    }

    public long calcularPosicao(long tamanhoReg, Aluno aluno)
            throws IOException{
        ByteBuffer bb = ByteBuffer.allocate((int)tamanhoReg);
        long posicao = this.getHash(aluno.getMatricula());
        this.canal.read(bb, posicao);
        Aluno verificarAluno = Conversor.toAluno(bb);
        // se está cheio, calcula nova posicao
        long mat = verificarAluno.getMatricula();
        long inc;
        if(mat > 0){
            return posicao;
        }else{
            inc = this.getIncremento(mat);
        }

        do{
            long calcPos = (posicao + inc) * tamanhoReg;
            this.canal.read(bb, calcPos);
            verificarAluno = Conversor.toAluno(bb);
            mat = verificarAluno.getMatricula();
            if(mat > 0){
                System.out.println("posicao " + posicao + " ocupada\n");
                posicao = -1;
            }
        }while(posicao <= 0);

        return posicao;
    }

    @Override
    public void addAluno(Aluno aluno) {
        long atualPos = 0;
        boolean aqui = false;
        Aluno alunoTmp;
        ByteBuffer bbtmp = ByteBuffer.allocate((int)tamanhoRegistro);

        try {
            long tamanhoCanal = this.canal.size() / tamanhoTabela;
            long hash = getHash(aluno.getMatricula());
            //Lê a posição do hash no arquivo
            this.canal.read(bbtmp,hash*200);
            alunoTmp = Conversor.toAluno(bbtmp);
            //Limpa o ByteBuffer e converte o aluno passado como parâmetro
            bbtmp.clear();
            bbtmp = Conversor.toByteBuffer(aluno);

            /*if(tamanhoCanal == 0){
                this.canal.write(Conversor.toByteBuffer(aluno));
            }*/

            //Verifica se a posição do arquivo está vazia
            if(alunoTmp.getMatricula() <= 0){
                //Se estiver salva o aluno na posição
                this.canal.write(bbtmp,hash);
            }
            else{
                //Se não estiver, conta qual dos 2 exige menos pulos e faz a troca
                int pulosAluno = contaPulos(aluno, hash, getIncremento(aluno.getMatricula()));
                int pulosAlunoTmp = contaPulos(alunoTmp, getHash(alunoTmp.getMatricula()),getIncremento(alunoTmp.getMatricula()));
                if(pulosAlunoTmp <= pulosAluno){
                    this.canal.write(bbtmp,(hash + (getIncremento(aluno.getMatricula())*pulosAluno))*200);
                }
                else{
                    ByteBuffer tmp;
                    tmp = Conversor.toByteBuffer(alunoTmp);
                    this.canal.write(tmp,(hash + (getIncremento(aluno.getMatricula())*pulosAlunoTmp))*200);
                    this.canal.write(bbtmp,hash*200);
                }
               // long posicaoReg = this.calcularPosicao(tamanhoTabela, aluno);



                /*
                do{
                    this.canal.read(bbtmp, atualPos*tamanhoRegistro);
                    posicaoReg = this.getHash(posicaoReg);

                    if(this.armazenamentoVazio(bbtmp, posicaoReg)){
                        this.canal.write(bbtmp, posicaoReg);

                    }else{
                        bbtmp.position(0);
                        posicaoReg = this.getIncremeto(posicaoReg);
                        atualPos = atualPos + posicaoReg;
                    }

                }while (tamanhoCanal > atualPos && !aqui);*/


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
        long contador;
        long repeticoes = (this.canal.size() / tamanhoRegistro);

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
    public long getIncremento(long matricula) {
        //return (this.getHash(matricula) % (tamanhoTabela-2))+1;
        return this.getHash((matricula/tamanhoTabela));
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
        return false;
    }

    public boolean espacoVazio(ByteBuffer bb, long posicao) {
        bb.position((int)posicao);
        Aluno a = Conversor.toAluno(bb);
        return a.getMatricula() <= 0;
    }

    public int contaPulos (Aluno aluno, long hash, long incremento){
        ByteBuffer bbtmp = ByteBuffer.allocate(Aluno.tamanho);
        Aluno alunoTmp;
        int contador = 0;
        try {
            do {
                hash += incremento;
                contador++;
                bbtmp.clear();
                bbtmp.put(new byte[200]);
                bbtmp.position(0);
                this.canal.read(bbtmp, hash * 200);
                alunoTmp = Conversor.toAluno(bbtmp);
            } while (alunoTmp.getMatricula() > 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contador;
    }

}
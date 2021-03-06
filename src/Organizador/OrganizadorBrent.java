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
    private long tamanhoTabela = ((long)12000017); //(long)11;//

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
                System.out.println("posicao " + posicao + " ocupada ("+posicao/tamanhoTabela+")\n");
                posicao = -1;
            }
        }while(posicao <= 0);

        return posicao;
    }

    @Override
    public void addAluno(Aluno aluno) {
        Aluno alunoTmp;
        ByteBuffer bbtmp = ByteBuffer.allocate((int)tamanhoRegistro);

        try {
            long hash = getHash(aluno.getMatricula());
            //Lê a posição do hash no arquivo
            long escreverEmPosicaoTabela = (hash*tamanhoRegistro);
            this.canal.read(bbtmp,escreverEmPosicaoTabela);
            bbtmp.flip();
            
            alunoTmp = Conversor.toAluno(bbtmp);
            //Limpa o ByteBuffer e converte o aluno passado como parâmetro
            bbtmp.clear();
            bbtmp.put(new byte[200]);
            bbtmp.position(0);
            bbtmp = Conversor.toByteBuffer(aluno);
            
            //Verifica se a posição do arquivo está vazia
            if(this.canal.size() <= 0 || alunoTmp.getMatricula() <= 0){
                //Se estiver salva o aluno na posição
                this.canal.write(bbtmp, escreverEmPosicaoTabela);
            }
            else{
                //Se não estiver, conta qual dos 2 exige menos pulos e faz a troca
                long matAluno = aluno.getMatricula();
                long matTmp = alunoTmp.getMatricula();
                int pulosAluno = contaPulos(aluno, hash, getIncremento(matAluno));
                int pulosAlunoTmp = contaPulos(alunoTmp, getHash(matTmp),getIncremento(matTmp));
                
                if(pulosAlunoTmp >= pulosAluno){
                    long posicao = (hash + (getIncremento(matAluno)*pulosAluno));
                    this.canal.write(bbtmp,getHash(posicao)*tamanhoRegistro);
                }
                else{
                    ByteBuffer tmp;
                    tmp = Conversor.toByteBuffer(alunoTmp);
                    long posicao = (hash + (getIncremento(matTmp)*pulosAlunoTmp));
                    this.canal.write(tmp,getHash(posicao)*tamanhoRegistro);
                    this.canal.write(bbtmp,getHash(hash)*tamanhoRegistro);
                }

            }

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao adicionar o aluno");
            e.printStackTrace();
        }
    }

    @Override
    public Aluno getAluno(long matricula){
        Aluno a = null; //new Aluno();
        long hash = getHash(matricula);
        long posicao = hash;
        ByteBuffer bb = ByteBuffer.allocate((int)tamanhoRegistro);
        boolean achou = false;
        try {
            this.canal.read(bb, hash*tamanhoRegistro);
            a = Conversor.toAluno(bb);
            if(matricula < 1){
                return null;
            }
            if(a.getMatricula() == matricula){
                return a;
            }
            else{
                long incremento = getIncremento(matricula);
                long iniciou = hash;
                do{
                    bb.clear();
                    bb.put(new byte[(int)tamanhoRegistro]);
                    bb.flip();
                    posicao += incremento;
                    posicao = getHash(posicao);
                    this.canal.read(bb,posicao*tamanhoRegistro);
                    a = Conversor.toAluno(bb);
                    if(a.getMatricula() == matricula)
                        achou = true;
                }
                while(!achou && posicao != hash);
                if(!achou) a = null;
                return a;
            }
        } catch (IOException ex) {
            System.err.println("Erro ao pegar matricula.");
        }
        return a;
    }
    
    public void getAll(){
        ByteBuffer temp = ByteBuffer.allocate((int)tamanhoRegistro);
        Aluno teste;
        try {
            for (long i = 0; i < canal.size(); i += tamanhoRegistro){
                canal.read(temp,i);
                teste = Conversor.toAluno(temp);
                if(!(teste.getMatricula() <= 0)){
                    System.out.println("\nposicao " + i + " ("+(i/tamanhoRegistro)+")\n"+teste);
                }
                temp.clear();
                temp.put(new byte[200]);
                temp.position(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Aluno delAluno(long matricula){
        Aluno a = null; //new Aluno();
        long hash = getHash(matricula);
        long posicao = hash;
        ByteBuffer bb = ByteBuffer.allocate((int)tamanhoRegistro);
        boolean achou = false;
        try {
            if(matricula < 1){
                return a;
            }
            bb.position(0);
            this.canal.read(bb, hash*tamanhoRegistro);
            bb.position(0);
            a = Conversor.toAluno(bb);
            if(a.getMatricula() == matricula){
                bb.position(0);
                Aluno b = new Aluno((long)-1,a.getNome(),a.getRua(),a.getEmail(),a.getCurso());
                bb = Conversor.toByteBuffer(b);
                this.canal.write(bb, hash*tamanhoRegistro);
                return a;
            }
            else{
                long incremento = getIncremento(matricula);
                int contador = 0;
                do{
                    bb.position(0);
                    bb.clear();
                    bb.put(new byte[(int)tamanhoRegistro]);
                    bb.flip();
                    posicao += incremento;
                    posicao = getHash(posicao);
                    bb.position(0);
                    long calcPosAtual = posicao*tamanhoRegistro;
                    this.canal.read(bb,calcPosAtual);
                    bb.position(0);
                    a = Conversor.toAluno(bb);
                    contador++;
                    if(a.getMatricula() == matricula){
                        Aluno b = new Aluno((long)-1,a.getNome(),a.getRua(),a.getEmail(),a.getCurso());
                        this.canal.write(Conversor.toByteBuffer(b),calcPosAtual);
                        return a;
                    }

                }
                while(!achou && posicao != hash && contador < 200);
                if(!achou) a = null;
                return a;
            }
        } catch (IOException ex) {
            System.err.println("Erro ao pegar matricula.");
        }
        return a;
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
        return this.getHash((long)(matricula/tamanhoTabela));
    }

    public int contaPulos (Aluno aluno, long hash, long incremento){
        ByteBuffer bbtmp = ByteBuffer.allocate(Aluno.tamanho);
        Aluno alunoTmp;
        int contador = 0;
        try {
            do {
                hash += incremento;
                hash = (hash > tamanhoTabela) ? this.getHash(hash) : hash;
                ++contador;
                bbtmp.clear();
                bbtmp.put(new byte[(int)tamanhoRegistro]);
                bbtmp.position(0);
                this.canal.read(bbtmp, hash * tamanhoRegistro);
                alunoTmp = Conversor.toAluno(bbtmp);
            } while (alunoTmp.getMatricula() > 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contador;
    }

}
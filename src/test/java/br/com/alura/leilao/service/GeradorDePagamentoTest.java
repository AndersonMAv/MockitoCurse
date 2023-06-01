package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

public class GeradorDePagamentoTest {

    private GeradorDePagamento gerador;
    
    @Mock
    private PagamentoDao pagamentoDao;
    
    @Mock
    private Clock clock;

    //Captura objeto criado pela chamada de algum metodo.
    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    public void beforeEach() {
        
        //Necessario inicializar as anotações do mockito.
        MockitoAnnotations.initMocks( this );
        this.gerador = new GeradorDePagamento( pagamentoDao, clock );
    }
    
    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilaoVencimentoSegunda() {
        
        /**
         * Teste dias da semana. Segunda a Quinta
         */
        {
            Leilao leilao = leilao();
            Lance vencedor = leilao.getLanceVencedor();
            //Ano mes dia
            LocalDate dataSeg = LocalDate.of( 2023, 6, 1 );
            Instant instantSeg = dataSeg.atStartOfDay( ZoneId.systemDefault() ).toInstant();

            Mockito.when( clock.instant() ).thenReturn( instantSeg );
            Mockito.when( clock.getZone() ).thenReturn( ZoneId.systemDefault() );

            gerador.gerarPagamento( vencedor );

            //Verificar se o metodo salvar foi chamado. Usando como argumento o captor.
            Mockito.verify( pagamentoDao ).salvar( captor.capture() );

            Pagamento pagamento = captor.getValue();

            Assert.assertEquals( LocalDate.now().plusDays(1), pagamento.getVencimento() );
            Assert.assertEquals( vencedor.getValor(), pagamento.getValor() );
            Assert.assertEquals( vencedor.getUsuario(), pagamento.getUsuario() );
            Assert.assertEquals( leilao, pagamento.getLeilao() );
            Assert.assertFalse( pagamento.getPago() );
        }
        
        /**
         * Teste Sextas
         */
        

        /**
         * Teste Sabado
         */


        /**
         * Teste Doming
         */

        
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilaoVencimentoSexta() {
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        LocalDate dataSexta = LocalDate.of( 2023, 6, 2 );
        Instant instantSexta = dataSexta.atStartOfDay( ZoneId.systemDefault() ).toInstant();

        Mockito.when( clock.instant() ).thenReturn( instantSexta );
        Mockito.when( clock.getZone() ).thenReturn( ZoneId.systemDefault() );

        gerador.gerarPagamento( vencedor );

        Mockito.verify( pagamentoDao ).salvar( captor.capture() );

        Pagamento pagamento = captor.getValue();

        Assert.assertEquals( LocalDate.of( 2023, 6, 2 ).plusDays(3), pagamento.getVencimento() );
        Assert.assertEquals( vencedor.getValor(), pagamento.getValor() );
        Assert.assertEquals( vencedor.getUsuario(), pagamento.getUsuario() );
        Assert.assertEquals( leilao, pagamento.getLeilao() );
        Assert.assertFalse( pagamento.getPago() );
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilaoVencimentoSabado() {
        
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();

        LocalDate dataSabado = LocalDate.of( 2023, 6, 3 );
        Instant instantSabado = dataSabado.atStartOfDay( ZoneId.systemDefault() ).toInstant();

        Mockito.when( clock.instant() ).thenReturn( instantSabado );
        Mockito.when( clock.getZone() ).thenReturn( ZoneId.systemDefault() );

        gerador.gerarPagamento( vencedor );

        Mockito.verify( pagamentoDao ).salvar( captor.capture() );

        Pagamento pagamento = captor.getValue();

        Assert.assertEquals( LocalDate.of( 2023, 6, 3 ).plusDays(2), pagamento.getVencimento() );
        Assert.assertEquals( vencedor.getValor(), pagamento.getValor() );
        Assert.assertEquals( vencedor.getUsuario(), pagamento.getUsuario() );
        Assert.assertEquals( leilao, pagamento.getLeilao() );
        Assert.assertFalse( pagamento.getPago() );
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilaoVencimentoDomingo() {
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();

        LocalDate dataDomingo = LocalDate.of( 2023, 6, 4 );
        Instant instantDomingo = dataDomingo.atStartOfDay( ZoneId.systemDefault() ).toInstant();

        Mockito.when( clock.instant() ).thenReturn( instantDomingo );
        Mockito.when( clock.getZone() ).thenReturn( ZoneId.systemDefault() );

        gerador.gerarPagamento( vencedor );

        Mockito.verify( pagamentoDao ).salvar( captor.capture() );

        Pagamento pagamento = captor.getValue();

        Assert.assertEquals( LocalDate.of( 2023, 6, 4 ).plusDays(1), pagamento.getVencimento() );
        Assert.assertEquals( vencedor.getValor(), pagamento.getValor() );
        Assert.assertEquals( vencedor.getUsuario(), pagamento.getUsuario() );
        Assert.assertEquals( leilao, pagamento.getLeilao() );
        Assert.assertFalse( pagamento.getPago() );
    }
    

    private Leilao leilao() {

        Leilao leilao = new Leilao("Celular",
                        new BigDecimal("500"),
                        new Usuario("Fulano"));

        Lance segundo = new Lance(new Usuario("Ciclano"),
                        new BigDecimal("900"));

        leilao.propoe(segundo);
        leilao.setLanceVencedor(segundo);
    
        return leilao;

    }
    
}

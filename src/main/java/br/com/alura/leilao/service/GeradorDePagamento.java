package br.com.alura.leilao.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;
	//Este clock define o relogio do sistema. adicionado para poder controlar definir isso nos testes
	private Clock clock;

	@Autowired
	public GeradorDePagamento( PagamentoDao pagamentos, Clock clock ) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock);
		Pagamento pagamento = new Pagamento(lanceVencedor, getVencimento( vencimento ) );
		this.pagamentos.salvar(pagamento);
	}

	private LocalDate getVencimento( LocalDate data ) {
		DayOfWeek diaDeSemana = data.getDayOfWeek();
		
		if( diaDeSemana == DayOfWeek.FRIDAY ) {
			return data.plusDays(3);
		}
		else if( diaDeSemana == DayOfWeek.SATURDAY ) {
			return data.plusDays(2);
		}
		return data.plusDays(1);
	}

}

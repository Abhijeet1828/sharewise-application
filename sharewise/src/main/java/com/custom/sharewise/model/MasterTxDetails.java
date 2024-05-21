package com.custom.sharewise.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity(name = "master_tx_details")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterTxDetails extends BaseModel implements Serializable {

	private static final long serialVersionUID = -7555188412170243892L;

	@Id
	@Column(name = "master_tx_details_id")
	private Long masterTxDetailsId;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "request_type")
	private String requestType;

	@Column(name = "group_expenses_id")
	private Long groupExpensesId;

	@Column(name = "group_transactions_id")
	private Long groupTransactionsId;

	@Column(name = "request")
	private String request;

}

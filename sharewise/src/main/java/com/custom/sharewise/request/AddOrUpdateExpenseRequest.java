package com.custom.sharewise.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.custom.common.utilities.validators.SafeInput;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddOrUpdateExpenseRequest implements Serializable {

	private static final long serialVersionUID = -8961988988410077237L;

	@Positive(groups = OnUpdate.class)
	@NotNull(groups = OnUpdate.class)
	private Long groupExpensesId;

	@Positive(groups = { OnCreate.class, OnUpdate.class })
	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	private Long groupId;

	@Positive(groups = { OnCreate.class, OnUpdate.class })
	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	private Long paidBy;

	@NotEmpty(groups = { OnCreate.class, OnUpdate.class })
	private List<Long> splitBetween;

	@Positive(groups = { OnCreate.class, OnUpdate.class })
	@NotNull(groups = { OnCreate.class, OnUpdate.class })
	private BigDecimal totalAmount;

	@SafeInput(groups = { OnCreate.class, OnUpdate.class })
	private String category;

}
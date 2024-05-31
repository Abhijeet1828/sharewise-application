package com.custom.sharewise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.constants.Constants;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.model.GroupTransactions;
import com.custom.sharewise.repository.GroupTransactionsRepository;
import com.custom.sharewise.request.AddOrUpdateExpenseRequest;
import com.custom.sharewise.request.AddTransactionRequest;
import com.custom.sharewise.validation.BusinessValidationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class GroupTransactionsServiceImpl implements GroupTransactionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupTransactionsServiceImpl.class);

	private final GroupTransactionsRepository groupTransactionsRepository;
	private final BusinessValidationService businessValidationService;
	private final ModelMapper modelMapper;

	@Override
	public void addGroupTransaction(AddOrUpdateExpenseRequest addExpenseRequest, Long groupExpensesId)
			throws CommonException {
		try {
			BigDecimal perPersonShare = addExpenseRequest.getTotalAmount()
					.divide(new BigDecimal(addExpenseRequest.getSplitBetween().size()), 2, RoundingMode.UP);

			List<GroupTransactions> groupTransactionList = new ArrayList<>();

			for (Long userId : addExpenseRequest.getSplitBetween()) {
				// Not Adding user who paid for the transaction
				if (userId.equals(addExpenseRequest.getPaidBy())) {
					continue;
				}

				GroupTransactions groupTransactions = GroupTransactions.builder()
						.groupId(addExpenseRequest.getGroupId()).groupExpensesId(groupExpensesId)
						.paidBy(addExpenseRequest.getPaidBy()).paidTo(userId).amount(perPersonShare)
						.transactionType(Constants.TRANSACTION_TYPE_GROUP_EXPENSE).isDeleted(false).build();
				groupTransactions.setCreatedTimestamp(new Date());
				groupTransactions.setModifiedTimestamp(new Date());

				groupTransactionList.add(groupTransactions);
			}

			groupTransactionsRepository.saveAll(groupTransactionList);
		} catch (Exception e) {
			LOGGER.error("Exception in addGroupTransaction", e);
			throw new CommonException(FailureConstants.ADD_GROUP_TRANSACTION_ERROR.getFailureCode(),
					FailureConstants.ADD_GROUP_TRANSACTION_ERROR.getFailureMsg());
		}
	}

	@Override
	public Object addUserPaymentTransaction(AddTransactionRequest addTransactionRequest) throws CommonException {
		try {
			Map<String, Object> validations = new HashMap<>();
			validations.put(Constants.VALIDATION_GROUP_ID, addTransactionRequest.getGroupId());
			validations.put(Constants.VALIDATION_USER_GROUP, new UserGroupDto(addTransactionRequest.getGroupId(), null,
					List.of(addTransactionRequest.getPaidBy(), addTransactionRequest.getPaidTo())));

			businessValidationService.validate(validations);

			modelMapper.getConfiguration().setSkipNullEnabled(true);
			modelMapper.typeMap(AddTransactionRequest.class, GroupTransactions.class);

			GroupTransactions groupTransactions = modelMapper.map(addTransactionRequest, GroupTransactions.class);
			groupTransactions.setTransactionType(Constants.TRANSACTION_TYPE_USER_PAYMENT);
			groupTransactions.setIsDeleted(false);
			groupTransactions.setCreatedTimestamp(new Date());
			groupTransactions.setModifiedTimestamp(new Date());

			return groupTransactionsRepository.save(groupTransactions);
		} catch (Exception e) {
			LOGGER.error("Exception in addUserPaymentTransaction", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.ADD_GROUP_TRANSACTION_ERROR.getFailureCode(),
						FailureConstants.ADD_GROUP_TRANSACTION_ERROR.getFailureMsg());
		}
	}

	@Override
	public void removeGroupTransactions(Long groupExpensesId) throws CommonException {
		try {
			Long deletedRecords = groupTransactionsRepository.deleteByGroupExpensesId(groupExpensesId);

			LOGGER.info("Deleted {} records for GroupExpensesId {}", deletedRecords, groupExpensesId);
		} catch (Exception e) {
			LOGGER.error("Exception in removeGroupTransactions", e);
			throw new CommonException(FailureConstants.DELETE_GROUP_TRANSACTIONS_ERROR.getFailureCode(),
					FailureConstants.DELETE_GROUP_TRANSACTIONS_ERROR.getFailureMsg());
		}
	}

	@Override
	public void softDeleteGroupTransactions(Long groupExpensesId) throws CommonException {
		try {
			List<GroupTransactions> groupTransactionsList = groupTransactionsRepository
					.findAllByGroupExpensesIdAndIsDeletedFalse(groupExpensesId);

			for (GroupTransactions transaction : groupTransactionsList) {
				transaction.setIsDeleted(true);
				transaction.setModifiedTimestamp(new Date());
			}

			groupTransactionsRepository.saveAll(groupTransactionsList);
		} catch (Exception e) {
			LOGGER.error("Exception in softDeleteGroupTransactions", e);
			throw new CommonException(FailureConstants.DELETE_GROUP_TRANSACTIONS_ERROR.getFailureCode(),
					FailureConstants.DELETE_GROUP_TRANSACTIONS_ERROR.getFailureMsg());
		}
	}

	@Override
	public void restoreGroupTransaction(Long groupExpensesId) throws CommonException {
		try {
			List<GroupTransactions> groupTransactionsList = groupTransactionsRepository
					.findAllByGroupExpensesIdAndIsDeletedTrue(groupExpensesId);

			for (GroupTransactions transaction : groupTransactionsList) {
				transaction.setIsDeleted(false);
				transaction.setModifiedTimestamp(new Date());
			}

			groupTransactionsRepository.saveAll(groupTransactionsList);
		} catch (Exception e) {
			LOGGER.error("Exception in restoreGroupTransaction", e);
			throw new CommonException(FailureConstants.RESTORE_GROUP_TRANSACTION_ERROR.getFailureCode(),
					FailureConstants.RESTORE_GROUP_TRANSACTION_ERROR.getFailureMsg());
		}
	}
}

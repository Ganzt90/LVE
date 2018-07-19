/**************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                               *
 * This program is free software; you can redistribute it and/or modify it    		  *
 * under the terms version 2 or later of the GNU General Public License as published  *
 * by the Free Software Foundation. This program is distributed in the hope           *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied         *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                   *
 * See the GNU General Public License for more details.                               *
 * You should have received a copy of the GNU General Public License along            *
 * with this program; if not, printLine to the Free Software Foundation, Inc.,        *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                             *
 * For the text or an alternative of this public license, you may reach us            *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, S.A. All Rights Reserved.  *
 * Contributor: Yamel Senih ysenih@erpya.com                                          *
 * Contributor: Carlos Parada cparada@erpya.com                                       *
 * See: www.erpya.com                                                                 *
 *************************************************************************************/
package org.lve.bank.exp;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPartner;
import org.compiere.model.MBank;
import org.compiere.model.MBankAccount;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPaySelectionCheck;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentBatch;
import org.compiere.util.Env;
import org.compiere.util.PaymentExportList;
import org.compiere.util.Util;

/**
 * This class is used like a parent class for make helper method used on 
 * Location for Venezuela, if you use LVE, you can extend it instead of PaymentExportList class
 * 	@author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
 *		<a href="https://github.com/adempiere/LVE/issues/1">
 * 		@see FR [ 1 ] Initial commit</a>
 */
public abstract class LVEPaymentExportList extends PaymentExportList {
	
	/**
	 * Left padding optional fixed length
	 * @param text
	 * @param length
	 * @param padding
	 * @param isFixedLength
	 * @return
	 */
	public String leftPadding(String text, int length, String padding, boolean isFixedLength) {
		return leftPadding(text, length, padding, isFixedLength, false, null);
	}
	
	/**
	 * Right padding optional fixed length
	 * @param text
	 * @param length
	 * @param padding
	 * @param isFixedLength
	 * @return
	 */
	public String rightPadding(String text, int length, String padding, boolean isFixedLength) {
		return rightPadding(text, length, padding, isFixedLength, false, null);
	}
	
	/**
	 * Left padding, it also cut text if it is necessary
	 * @param text
	 * @param length
	 * @param padding
	 * @param isFixedLength
	 * @param isMandatory
	 * @param mandatoryMessage
	 * @return
	 */
	public String leftPadding(String text, int length, String padding, boolean isFixedLength, boolean isMandatory, String mandatoryMessage) {
		return addPadding(text, length, padding, isFixedLength, isMandatory, mandatoryMessage, true);
	}
	
	/**
	 * Right padding, it also cut text if it is necessary
	 * @param text
	 * @param length
	 * @param padding
	 * @param isFixedLength
	 * @param isMandatory
	 * @param mandatoryMessage
	 * @return
	 */
	public String rightPadding(String text, int length, String padding, boolean isFixedLength, boolean isMandatory, String mandatoryMessage) {
		return addPadding(text, length, padding, isFixedLength, isMandatory, mandatoryMessage, false);
	}
	
	/**
	 * Add Padding, for using internal
	 * @param text
	 * @param length
	 * @param padding
	 * @param isFixedLength
	 * @param isMandatory
	 * @param mandatoryMessage
	 * @param isLeft
	 * @return
	 */
	private String addPadding(String text, int length, String padding, boolean isFixedLength, boolean isMandatory, String mandatoryMessage, boolean isLeft) {
		if(Util.isEmpty(text)) {
			if(isMandatory
					&& !Util.isEmpty(mandatoryMessage)) {
				addError(mandatoryMessage);
			}
			//	Return void text
			return text;
		}
		String processedText = text;
		//	Process it
		if(isFixedLength) {
			processedText = processedText.substring(0, processedText.length() >= length? length: processedText.length());
		}
		//	For padding 
		if(isLeft) {
			processedText = leftPadding(processedText, length, padding);
		} else {
			processedText = rightPadding(processedText, length, padding);
		}
		//	Return
		return processedText;
	}

	/**
	 * Used for verification
	 * @param bankAccount
	 * @param payments
	 * @param file
	 * @param error
	 * @return
	 */
	public int exportToFileAsVerification(MBankAccount bankAccount, List<MPayment> payments, File file, StringBuffer error) {
		return 0;
	}
	
	/**
	 * Used for account Enrollment 
	 * @param bPartnerList
	 * @param file
	 * @param error
	 * @return
	 */
	public int exportToFileAsEnrollmentRequest(List<MBPartner> bPartnerList, File file, StringBuffer error) {
		return 0;
	}
	
	/**
	 * Used for account Enrollment 
	 * @param bankAccount
	 * @param bPartnerList
	 * @param bPartnerAccountList
	 * @param isEnroll
	 * @param file
	 * @param error
	 * @return
	 */
	public int exportToFileAsEnrollment(MBankAccount bankAccount, List<MBPBankAccount> bPartnerAccountList, boolean isEnroll, File file, StringBuffer error) {
		return 0;
	}
	
	/**
	 * 
	 * @param checks
	 * @param file
	 * @param error
	 * @return
	 */
	public int exportToFileAsPayroll(List<MPaySelectionCheck> checks, File file, StringBuffer error) {
		return 0;
	}
	
	/**
	 * Open File from Payment Selection
	 * @param file
	 * @param checks
	 */
	public void openFileWriter(File file, List<MPaySelectionCheck> checks) {
		MPaySelectionCheck check = checks.get(0);
		MPaySelection paymentSelection = check.getParent();
		MBankAccount bankAccount = MBankAccount.get(Env.getCtx(), paymentSelection.getC_BankAccount_ID());
		MBank bank = MBank.get(Env.getCtx(), bankAccount.getC_Bank_ID());
		String fileName = getFileName(file, bank.getName(), paymentSelection.getDocumentNo());
		openFileWriter(file, fileName);
	}
	
	/**
	 * Open File from Payments
	 * @param file
	 * @param bankAccount
	 * @param payments
	 * @param suffix
	 */
	public void openFileWriter(File file, MBankAccount bankAccount, List<MPayment> payments, String suffix) {
		if(Util.isEmpty(suffix)) {
			suffix = "";
		}
		MPayment firstPayment = payments.get(0);
		MPaymentBatch paymentBatch = (MPaymentBatch) firstPayment.getC_PaymentBatch();
		MBank bank = MBank.get(Env.getCtx(), bankAccount.getC_Bank_ID());
		String fileName = getFileName(file, bank.getName(), paymentBatch.getDocumentNo() + suffix);
		openFileWriter(file, fileName);
	}
	
	/**
	 * Open file with new name and delete if exist
	 * @param file
	 * @param bankName
	 * @param documentNo
	 */
	public void openFileWriter(File file, String bankName, String documentNo) {
		String fileName = getFileName(file, bankName, documentNo);
		openFileWriter(file, fileName);
	}
	
	/**
	 * Open File with a new name
	 * @param file
	 * @param newName
	 */
	public void openFileWriter(File file, String newName) {
		String fileName = getParentFileName(file);
		fileName = fileName + File.separator + newName;
		File newFile = new File(fileName);
		deleteIfExist(newFile);
		openFileWriter(newFile);
	}
	
	/**
	 * Get Parent File Path
	 * @param file
	 * @return
	 */
	public String getParentFileName(File file) {
		StringBuffer pathName = new StringBuffer();
		if(file.isFile() || !file.exists()) {
			pathName.append(file.getParent());
		} else {
			pathName.append(file.getAbsolutePath());
		}
		//	Return
		return pathName.toString();
	}
	
	/**
	 * Get File Name for document
	 * @param file
	 * @param bankName
	 * @param documentNo
	 * @return
	 */
	public String getFileName(File file, String bankName, String documentNo) {
		if(file == null) {
			return null;
		}
		//	Extension
		String extension = ".txt";
		//	Set new File Name
		StringBuffer pathName = new StringBuffer(getParentFileName(file));
		//	Add Separator
		pathName.append(File.separator)
				.append(bankName)
				.append("_")
				.append(documentNo)
				.append(extension);
		//	Return
		return pathName.toString().replace(" ", "_");
	}
	
	/**
	 * Get business partner account information as PO
	 * @param payment
	 * @param defaultWhenNull if payment selection account is null try get a account of bp
	 * @return
	 */
	public MBPBankAccount getBPAccountInfo(MPayment payment, boolean defaultWhenNull) {
		if(payment.getC_BP_BankAccount_ID() != 0) {
			return (MBPBankAccount) payment.getC_BP_BankAccount();
		}
		//	Get any bp account
		if(defaultWhenNull) {
			List<MBPBankAccount> bpAccountList = MBPBankAccount.getByPartner(Env.getCtx(), payment.getC_BPartner_ID());
			if(bpAccountList == null
					|| bpAccountList.size() == 0) {
				return null;
			}
			//	Get 
			Optional<MBPBankAccount> first = bpAccountList.stream().filter(account -> account.isACH()).findFirst();
			if(first.isPresent()) {
				return first.get();
			} else {
				bpAccountList.get(0);
			}
		}
		//	default
		return null;
	}
	
}

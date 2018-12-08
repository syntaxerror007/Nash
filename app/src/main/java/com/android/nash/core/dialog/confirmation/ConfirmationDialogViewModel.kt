package com.android.nash.core.dialog.confirmation

import com.android.nash.core.CoreViewModel

data class ConfirmationDialogViewModel(var dialogTitle: String, var dialogMessage: String, var yesMessage: String, var noMessage: String, var onNoClicked: () -> Unit, var onYesClicked: () -> Unit) : CoreViewModel()
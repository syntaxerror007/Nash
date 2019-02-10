package com.nash.android.core.dialog.confirmation

import com.nash.android.core.CoreViewModel

data class ConfirmationDialogViewModel(var dialogTitle: String, var dialogMessage: String, var yesMessage: String, var noMessage: String, var onNoClicked: () -> Unit, var onYesClicked: () -> Unit) : CoreViewModel()
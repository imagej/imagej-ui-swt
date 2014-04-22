/*
 * #%L
 * SciJava UI components for Eclipse SWT.
 * %%
 * Copyright (C) 2011 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.ui.swt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.scijava.module.Module;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.AbstractInputHarvesterPlugin;
import org.scijava.ui.UIService;
import org.scijava.ui.UserInterface;
import org.scijava.ui.swt.SWTUI;
import org.scijava.widget.InputHarvester;
import org.scijava.widget.InputPanel;

/**
 * SWTInputHarvester is an {@link InputHarvester} that collects input parameter
 * values from the user using an {@link SWTInputPanel} dialog box.
 * 
 * @author Curtis Rueden
 * @author Barry DeZonia
 */
@Plugin(type = PreprocessorPlugin.class, priority = InputHarvester.PRIORITY)
public class SWTInputHarvester extends
	AbstractInputHarvesterPlugin<Composite, Composite>
{

	@Parameter
	private UIService uiService;

	// -- InputHarvester methods --

	@Override
	public SWTInputPanel createInputPanel() {
		return new SWTInputPanel(null);
	}

	@Override
	public boolean harvestInputs(
		final InputPanel<Composite, Composite> inputPanel, final Module module)
	{
		final Composite pane = inputPanel.getComponent();

		// TODO - obtain handle on parent SWTMainFrame somehow
		final Shell dialog = new Shell(getDisplay(), SWT.DIALOG_TRIM);
		pane.setParent(dialog);
		// TODO - use scroll bars in case there are many widgets
		final Button okButton = new Button(dialog, SWT.PUSH);
		okButton.setText("&OK");
		final Button cancelButton = new Button(dialog, SWT.PUSH);
		cancelButton.setText("&Cancel");

		final FormLayout form = new FormLayout();
		form.marginWidth = form.marginHeight = 8;
		dialog.setLayout(form);
		final FormData okData = new FormData();
		okData.top = new FormAttachment(pane, 8);
		okButton.setLayoutData(okData);
		final FormData cancelData = new FormData();
		cancelData.left = new FormAttachment(okButton, 8);
		cancelData.top = new FormAttachment(okButton, 0, SWT.TOP);
		cancelButton.setLayoutData(cancelData);

		dialog.setDefaultButton(okButton);
		dialog.pack();
		dialog.open();

		// TODO - check whether OK or Cancel was pushed
		return true;
	}

	// -- Internal methods --

	@Override
	protected String getUI() {
		return SWTUI.NAME;
	}

	// -- Helper methods --

	private Display getDisplay() {
		final UserInterface ui = uiService.getDefaultUI();
		if (!(ui instanceof SWTUI)) {
			throw new IllegalStateException("Invalid UI: " + ui.getClass().getName());
		}
		final SWTUI swtUI = (SWTUI) ui;
		return swtUI.getApplicationFrame().getDisplay();
	}

}

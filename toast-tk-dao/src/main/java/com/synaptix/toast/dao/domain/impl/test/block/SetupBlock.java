package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.impl.test.block.line.BlockLine;

@Embedded
public class SetupBlock implements IBlock {

	private List<BlockLine> blockLines;

	private BlockLine columns;

	private String fixtureName;

	private ITestResult testResult;

	/**
	 * 
	 */
	public SetupBlock() {
		blockLines = new ArrayList<BlockLine>();
	}

	public List<BlockLine> getBlockLines() {
		return blockLines;
	}

	public void setBlockLines(
		List<BlockLine> blockLines) {
		this.blockLines = blockLines;
	}

	public BlockLine getColumns() {
		return columns;
	}

	public void setColumns(
		BlockLine columns) {
		this.columns = columns;
	}

	public String getFixtureName() {
		return fixtureName;
	}

	public void setFixtureName(
		String fixtureName) {
		this.fixtureName = fixtureName;
	}

	public void addLine(
		List<String> cells) {
		blockLines.add(new BlockLine(cells));
	}

	public void addLine(
		BlockLine line) {
		blockLines.add(line);
	}

	public ITestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(
			ITestResult testResult) {
		this.testResult = testResult;
	}

	@Override
	public String getBlockType() {
		return "setup";
	}


	@Override
	public int getHeaderSize() {
		return 0;
	}
}

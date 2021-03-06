/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/AdoptOpenJDK/jitwatch/blob/master/LICENSE-BSD
 * Instructions: https://github.com/AdoptOpenJDK/jitwatch/wiki
 */
package org.adoptopenjdk.jitwatch.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.adoptopenjdk.jitwatch.model.IMetaMember;
import org.adoptopenjdk.jitwatch.model.JITDataModel;
import org.adoptopenjdk.jitwatch.model.MetaClass;
import org.adoptopenjdk.jitwatch.model.bytecode.BCAnnotationType;
import org.adoptopenjdk.jitwatch.model.bytecode.BytecodeAnnotationList;
import org.adoptopenjdk.jitwatch.model.bytecode.BytecodeAnnotations;
import org.adoptopenjdk.jitwatch.model.bytecode.LineAnnotation;
import org.junit.Test;

public class TestReport
{
	@Test
	public void testEliminatedAllocOnInlinedMethod()
	{
		String[] lines = new String[] {
				"<task_queued method='InlineElimAlloc outer (I)J' bytes='31' blocking='1' count='6' backedge_count='101376' stamp='0.242' comment='tiered' hot_count='6' compile_id='33' iicount='6'/>",
				"<nmethod stub_offset='912' dependencies_offset='1896' address='0x000000010b721090' method='InlineElimAlloc outer (I)J' level='4' count='6' backedge_count='101376' stamp='0.249' scopes_data_offset='1040' iicount='6' handler_table_offset='1904' oops_offset='936' entry='0x000000010b7211e0' size='1928' scopes_pcs_offset='1496' insts_offset='336' bytes='31' relocation_offset='296' compile_id='33' compiler='C2'/>",
				"<task method='InlineElimAlloc outer (I)J' bytes='31' blocking='1' count='6' backedge_count='101376' stamp='0.242' compile_id='33' iicount='6'>",
				"  <phase nodes='3' name='parse' stamp='0.242' live='3'>",
				"    <type name='long' id='722'/>",
				"    <type name='int' id='721'/>",
				"    <klass name='InlineElimAlloc' flags='1' id='831'/>",
				"    <method level='3' bytes='31' name='outer' flags='1' holder='831' arguments='721' id='832' compile_id='31' compiler='C1' iicount='6' return='722'/>",
				"    <parse method='832' stamp='0.242' uses='6'>",
				"      <bc code='162' bci='10'/>",
				"      <branch prob='4.88257e-05' not_taken='40960' taken='2' cnt='40962' target_bci='29'/>",
				"      <uncommon_trap reason='predicate' bci='13' action='maybe_recompile'/>",
				"      <uncommon_trap reason='loop_limit_check' bci='13' action='maybe_recompile'/>",
				"      <bc code='182' bci='18'/>",
				"      <method level='4' bytes='55' name='inner' flags='1' holder='831' arguments='721 721' id='834' compile_id='29' compiler='C2' iicount='5376' return='722'/>",
				"      <dependency x='834' ctxk='831' type='unique_concrete_method'/>",
				"      <call method='834' inline='1' count='40960' prof_factor='1'/>",
				"      <inline_success reason='inline (hot)'/>",
				"      <parse method='834' stamp='0.242' uses='40960'>",
				"        <bc code='79' bci='11'/>",
				"        <observe that='!need_range_check'/>",
				"        <bc code='79' bci='16'/>",
				"        <observe that='!need_range_check'/>",
				"        <bc code='183' bci='21'/>",
				"        <type name='void' id='723'/>",
				"        <klass name='java/util/Random' flags='1' id='835'/>",
				"        <method bytes='12' name='&lt;init&gt;' flags='1' holder='835' id='836' iicount='5376' return='723'/>",
				"        <call method='836' inline='1' count='5120' prof_factor='1'/>",
				"        <inline_success reason='inline (hot)'/>",
				"        <parse method='836' stamp='0.242' uses='5376'>",
				"          <bc code='184' bci='1'/>",
				"          <method bytes='29' name='seedUniquifier' flags='10' holder='835' id='839' iicount='5376' return='722'/>",
				"          <call method='839' inline='1' count='5121' prof_factor='1'/>",
				"          <inline_success reason='inline (hot)'/>",
				"          <parse method='839' stamp='0.242' uses='5376'>",
				"            <bc code='182' bci='3'/>",
				"            <klass name='java/util/concurrent/atomic/AtomicLong' flags='1' id='843'/>",
				"            <method level='3' bytes='5' name='get' flags='17' holder='843' id='845' compile_id='17' compiler='C1' iicount='10751' return='722'/>",
				"            <call method='845' inline='1' count='5121' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='845' stamp='0.242' uses='5376'>",
				"              <parse_done nodes='199' memory='55224' stamp='0.242' live='196'/>",
				"            </parse>",
				"            <bc code='182' bci='18'/>",
				"            <type name='boolean' id='715'/>",
				"            <method level='3' bytes='13' name='compareAndSet' flags='17' holder='843' arguments='722 722' id='846' compile_id='18' compiler='C1' iicount='10751' return='715'/>",
				"            <call method='846' inline='1' count='5121' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='846' stamp='0.242' uses='5376'>",
				"              <bc code='182' bci='9'/>",
				"              <klass name='java/lang/Object' flags='1' id='728'/>",
				"              <klass name='sun/misc/Unsafe' flags='17' id='795'/>",
				"              <method compile_kind='c2n' level='0' bytes='0' name='compareAndSwapLong' flags='273' holder='795' arguments='728 722 722 722' id='851' compile_id='15' iicount='256' return='715'/>",
				"              <call method='851' inline='1' count='0' prof_factor='0.500046'/>",
				"              <intrinsic nodes='19' id='_compareAndSwapLong'/>",
				"              <parse_done nodes='234' memory='60072' stamp='0.242' live='230'/>",
				"            </parse>",
				"            <bc code='153' bci='21'/>",
				"            <branch prob='never' not_taken='5376' taken='0' cnt='5376' target_bci='26'/>",
				"            <uncommon_trap reason='unstable_if' bci='21' action='reinterpret' comment='taken never'/>",
				"            <parse_done nodes='248' memory='62560' stamp='0.242' live='242'/>",
				"          </parse>",
				"          <bc code='184' bci='4'/>",
				"          <klass name='java/lang/System' flags='17' id='734'/>",
				"          <method compile_kind='c2n' level='0' bytes='0' name='nanoTime' flags='265' holder='734' id='840' compile_id='20' iicount='256' return='722'/>",
				"          <call method='840' inline='1' count='5121' prof_factor='1'/>",
				"          <intrinsic nodes='4' id='_nanoTime'/>",
				"          <bc code='183' bci='8'/>",
				"          <method level='4' bytes='53' name='&lt;init&gt;' flags='1' holder='835' arguments='722' id='841' compile_id='27' compiler='C2' iicount='5375' return='723'/>",
				"          <call method='841' inline='1' count='5121' prof_factor='1'/>",
				"          <inline_success reason='inline (hot)'/>",
				"          <parse method='841' stamp='0.242' uses='5376'>",
				"            <bc code='183' bci='1'/>",
				"            <method level='1' bytes='1' name='&lt;init&gt;' flags='1' holder='728' id='854' compile_id='24' compiler='C1' iicount='12620' return='723'/>",
				"            <call method='854' inline='1' count='5120' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='854' stamp='0.242' uses='5375'>",
				"              <parse_done nodes='287' memory='75632' stamp='0.242' live='280'/>",
				"            </parse>",
				"            <bc code='182' bci='10'/>",
				"            <klass name='java/lang/Class' flags='17' id='730'/>",
				"            <method compile_kind='c2n' level='0' bytes='0' name='getClass' flags='273' holder='728' id='855' compile_id='19' iicount='256' return='730'/>",
				"            <call method='855' inline='1' count='5120' prof_factor='1'/>",
				"            <intrinsic nodes='3' id='_getClass'/>",
				"            <bc code='166' bci='15'/>",
				"            <branch prob='never' not_taken='5375' taken='0' cnt='5375' target_bci='36'/>",
				"            <bc code='184' bci='24'/>",
				"            <method level='3' bytes='10' name='initialScramble' flags='10' holder='835' arguments='722' id='858' compile_id='23' compiler='C1' iicount='5375' return='722'/>",
				"            <call method='858' inline='1' count='5120' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='858' stamp='0.243' uses='5375'>",
				"              <parse_done nodes='341' memory='85288' stamp='0.243' live='333'/>",
				"            </parse>",
				"            <bc code='183' bci='27'/>",
				"            <method level='3' bytes='10' name='&lt;init&gt;' flags='1' holder='843' arguments='722' id='859' compile_id='21' compiler='C1' iicount='5376' return='723'/>",
				"            <call method='859' inline='1' count='5120' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='859' stamp='0.243' uses='5375'>",
				"              <bc code='183' bci='1'/>",
				"              <klass name='java/lang/Number' flags='1025' id='810'/>",
				"              <method level='3' bytes='5' name='&lt;init&gt;' flags='1' holder='810' id='863' compile_id='16' compiler='C1' iicount='5639' return='723'/>",
				"              <call method='863' inline='1' count='5120' prof_factor='0.999814'/>",
				"              <inline_success reason='inline (hot)'/>",
				"              <parse method='863' stamp='0.243' uses='5375'>",
				"                <bc code='183' bci='1'/>",
				"                <call method='854' inline='1' count='5255' prof_factor='0.953183'/>",
				"                <inline_success reason='inline (hot)'/>",
				"                <parse method='854' stamp='0.243' uses='5375'>",
				"                  <parse_done nodes='388' memory='93360' stamp='0.243' live='379'/>",
				"                </parse>",
				"                <parse_done nodes='391' memory='94336' stamp='0.243' live='381'/>",
				"              </parse>",
				"              <parse_done nodes='401' memory='95912' stamp='0.243' live='390'/>",
				"            </parse>",
				"            <parse_done nodes='419' memory='98496' stamp='0.243' live='407'/>",
				"          </parse>",
				"          <parse_done nodes='421' memory='99536' stamp='0.243' live='408'/>",
				"        </parse>",
				"        <bc code='182' bci='28'/>",
				"        <method bytes='14' name='nextBoolean' flags='1' holder='835' id='837' iicount='5376' return='715'/>",
				"        <dependency x='837' ctxk='835' type='unique_concrete_method'/>",
				"        <call method='837' inline='1' count='5120' prof_factor='1'/>",
				"        <inline_success reason='inline (hot)'/>",
				"        <parse method='837' stamp='0.243' uses='5376'>",
				"          <bc code='182' bci='2'/>",
				"          <method level='4' bytes='47' name='next' flags='4' holder='835' arguments='721' id='866' compile_id='28' compiler='C2' iicount='5375' return='721'/>",
				"          <dependency x='866' ctxk='835' type='unique_concrete_method'/>",
				"          <call method='866' inline='1' count='5121' prof_factor='1'/>",
				"          <inline_success reason='inline (hot)'/>",
				"          <parse method='866' stamp='0.243' uses='5376'>",
				"            <bc code='182' bci='8'/>",
				"            <call method='845' inline='1' count='5120' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='845' stamp='0.243' uses='5375'>",
				"              <uncommon_trap reason='null_check' bci='8' action='maybe_recompile'/>",
				"              <parse_done nodes='482' memory='110152' stamp='0.243' live='467'/>",
				"            </parse>",
				"            <bc code='182' bci='32'/>",
				"            <call method='846' inline='1' count='5120' prof_factor='1'/>",
				"            <inline_success reason='inline (hot)'/>",
				"            <parse method='846' stamp='0.243' uses='5375'>",
				"              <bc code='182' bci='9'/>",
				"              <call method='851' inline='1' count='0' prof_factor='0.499953'/>",
				"              <intrinsic nodes='19' id='_compareAndSwapLong'/>",
				"              <parse_done nodes='517' memory='127168' stamp='0.243' live='501'/>",
				"            </parse>",
				"            <bc code='153' bci='35'/>",
				"            <branch prob='never' not_taken='5375' taken='0' cnt='5375' target_bci='6'/>",
				"            <uncommon_trap reason='unstable_if' bci='35' action='reinterpret' comment='taken never'/>",
				"            <parse_done nodes='541' memory='131072' stamp='0.243' live='523'/>",
				"          </parse>",
				"          <bc code='153' bci='5'/>",
				"          <branch prob='0.493304' not_taken='2724' taken='2652' cnt='5376' target_bci='12'/>",
				"          <parse_done nodes='553' memory='132816' stamp='0.243' live='534'/>",
				"        </parse>",
				"        <bc code='153' bci='31'/>",
				"        <branch prob='0.493304' not_taken='2724' taken='2652' cnt='5376' target_bci='45'/>",
				"        <bc code='46' bci='38'/>",
				"        <observe that='!need_range_check'/>",
				"        <bc code='46' bci='49'/>",
				"        <observe that='!need_range_check'/>",
				"        <parse_done nodes='572' memory='135848' stamp='0.243' live='552'/>",
				"      </parse>",
				"      <bc code='162' bci='10'/>",
				"      <branch prob='4.88257e-05' not_taken='40960' taken='2' cnt='40959' target_bci='29'/>",
				"      <parse_done nodes='585' memory='139264' stamp='0.243' live='564'/>",
				"    </parse>",
				"    <phase_done nodes='587' name='parse' stamp='0.243' live='268'/>",
				"  </phase>",
				"  <phase nodes='587' name='optimizer' stamp='0.243' live='268'>",
				"    <phase nodes='587' name='idealLoop' stamp='0.243' live='248'>",
				"      <loop_tree>",
				"        <loop idx='593'>",
				"        </loop>",
				"      </loop_tree>",
				"      <phase_done nodes='594' name='idealLoop' stamp='0.243' live='246'/>",
				"    </phase>",
				"    <phase nodes='594' name='escapeAnalysis' stamp='0.243' live='246'>",
				"      <phase nodes='595' name='connectionGraph' stamp='0.243' live='247'>",
				"        <phase_done nodes='595' name='connectionGraph' stamp='0.243' live='247'/>",
				"      </phase>",
				"      <phase_done nodes='601' name='escapeAnalysis' stamp='0.243' live='253'/>",
				"    </phase>",
				"    <eliminate_allocation type='835'>",
				"      <jvms method='834' bci='17'/>",
				"      <jvms method='832' bci='18'/>",
				"    </eliminate_allocation>",
				"    <klass name='[I' flags='1041' id='825'/>",
				"    <eliminate_allocation type='825'>",
				"      <jvms method='834' bci='3'/>",
				"      <jvms method='832' bci='18'/>",
				"    </eliminate_allocation>",
				"    <phase nodes='609' name='idealLoop' stamp='0.244' live='184'>",
				"      <loop_tree>",
				"        <loop idx='593' inner_loop='1'>",
				"        </loop>",
				"      </loop_tree>",
				"      <phase_done nodes='613' name='idealLoop' stamp='0.244' live='164'/>",
				"    </phase>",
				"    <phase nodes='613' name='idealLoop' stamp='0.244' live='164'>",
				"      <loop_tree>",
				"        <loop idx='593' inner_loop='1'>",
				"        </loop>",
				"      </loop_tree>",
				"      <phase_done nodes='613' name='idealLoop' stamp='0.244' live='164'/>",
				"    </phase>",
				"    <phase nodes='613' name='idealLoop' stamp='0.244' live='164'>",
				"      <loop_tree>",
				"        <loop idx='593' inner_loop='1'>",
				"        </loop>",
				"      </loop_tree>",
				"      <phase_done nodes='613' name='idealLoop' stamp='0.244' live='164'/>",
				"    </phase>",
				"    <phase nodes='613' name='ccp' stamp='0.244' live='164'>",
				"      <phase_done nodes='613' name='ccp' stamp='0.244' live='164'/>",
				"    </phase>",
				"    <phase nodes='613' name='idealLoop' stamp='0.244' live='164'>",
				"      <loop_tree>",
				"        <loop idx='593' inner_loop='1'>",
				"        </loop>",
				"      </loop_tree>",
				"      <phase_done nodes='616' name='idealLoop' stamp='0.244' live='164'/>",
				"    </phase>",
				"    <phase_done nodes='671' name='optimizer' stamp='0.244' live='201'/>",
				"  </phase>",
				"  <phase nodes='671' name='matcher' stamp='0.244' live='201'>",
				"    <phase_done nodes='192' name='matcher' stamp='0.244' live='192'/>",
				"  </phase>",
				"  <phase nodes='212' name='regalloc' stamp='0.244' live='212'>",
				"    <regalloc success='1' attempts='0'/>",
				"    <phase_done nodes='282' name='regalloc' stamp='0.245' live='256'/>",
				"  </phase>",
				"  <phase nodes='282' name='output' stamp='0.245' live='256'>",
				"    <phase_done nodes='294' name='output' stamp='0.245' live='267'/>",
				"  </phase>",
				"  <dependency x='834' ctxk='831' type='unique_concrete_method'/>",
				"  <dependency x='837' ctxk='835' type='unique_concrete_method'/>",
				"  <dependency x='866' ctxk='835' type='unique_concrete_method'/>",
				"  <code_cache nmethods='33' free_code_cache='250509632' adapters='148' total_blobs='267'/>",
				"  <task_done inlined_bytes='273' success='1' count='6' backedge_count='101376' stamp='0.249' nmsize='600'/>",
				"</task>" };

		String[] bytecodeLinesOuter = new String[] {
				" 0: lconst_0        ",
				" 1: lstore_2        ",
				" 2: iconst_0        ",
				" 3: istore          4    ",
				" 5: iload           4    ",
				" 7: sipush          20000",
				"10: if_icmpge       29   ",
				"13: lload_2         ",
				"14: aload_0         ",
				"15: iload_1         ",
				"16: iload           4    ",
				"18: invokevirtual   #2   // Method inner:(II)J",
				"21: ladd            ",
				"22: lstore_2        ",
				"23: iinc            4, 1 ",
				"26: goto            5    ",
				"29: lload_2         ",
				"30: lreturn         " };

		String[] bytecodeLinesInner = new String[] {
				" 0: lconst_0        ",
				" 1: lstore_3        ",
				" 2: iconst_2        ",
				" 3: newarray        int  ",
				" 5: astore          5    ",
				" 7: aload           5    ",
				" 9: iconst_0        ",
				"10: iload_1         ",
				"11: iastore         ",
				"12: aload           5    ",
				"14: iconst_1        ",
				"15: iload_2         ",
				"16: iastore         ",
				"17: new             #3   // class java/util/Random",
				"20: dup             ",
				"21: invokespecial   #4   // Method java/util/Random.\"<init>\":()V",
				"24: astore          6    ",
				"26: aload           6    ",
				"28: invokevirtual   #5   // Method java/util/Random.nextBoolean:()Z",
				"31: ifeq            45   ",
				"34: lload_3         ",
				"35: aload           5    ",
				"37: iconst_0        ",
				"38: iaload          ",
				"39: i2l             ",
				"40: ladd            ",
				"41: lstore_3        ",
				"42: goto            53   ",
				"45: lload_3         ",
				"46: aload           5    ",
				"48: iconst_1        ",
				"49: iaload          ",
				"50: i2l             ",
				"51: ladd            ",
				"52: lstore_3        ",
				"53: lload_3         ",
				"54: lreturn         " };

		JITDataModel model = new JITDataModel();
		model.setVmVersionRelease("1.8.0");

		IMetaMember memberInner = UnitTestUtil.createTestMetaMember(model, "InlineElimAlloc", "inner",
				new Class[] { int.class, int.class }, long.class);

		IMetaMember memberOuter = UnitTestUtil.createTestMetaMember(model, "InlineElimAlloc", "outer", new Class[] { int.class },
				long.class);

		BytecodeAnnotations result = UnitTestUtil.buildAnnotations(false, true, model, memberOuter, lines, bytecodeLinesOuter);

		BytecodeAnnotationList listOuter = result.getAnnotationList(memberOuter);

		assertEquals(3, listOuter.annotatedLineCount());

		UnitTestUtil.checkAnnotation(listOuter, 10, "taken", BCAnnotationType.BRANCH);
		UnitTestUtil.checkAnnotation(listOuter, 13, "predicate", BCAnnotationType.UNCOMMON_TRAP);
		UnitTestUtil.checkAnnotation(listOuter, 13, "loop_limit_check", BCAnnotationType.UNCOMMON_TRAP);
		UnitTestUtil.checkAnnotation(listOuter, 18, "inline (hot)", BCAnnotationType.INLINE_SUCCESS);

		BytecodeAnnotationList listInner = result.getAnnotationList(memberInner);

		assertEquals(2, listInner.annotatedLineCount());

		UnitTestUtil.checkAnnotation(listInner, 3, "int", BCAnnotationType.ELIMINATED_ALLOCATION);
		UnitTestUtil.checkAnnotation(listInner, 17, "Random", BCAnnotationType.ELIMINATED_ALLOCATION);

		assertEquals(0, UnitTestUtil.unhandledTags.size());

		List<LineAnnotation> annotationsForOuterBCI18 = listOuter.getAnnotationsForBCI(18);
		assertEquals(1 + listInner.annotatedLineCount(), annotationsForOuterBCI18.size());
	}
}
package com.brkn.floater;

import android.accessibilityservice.AccessibilityService;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * SampleAccessibilityService
 *
 * @author sutharsha
 */
public class SampleAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo source = accessibilityEvent.getSource();
        if (source != null) {
            AccessibilityNodeInfo rowNode = getRootInActiveWindow();
            if (rowNode != null) {
                for (int i = 0; i < rowNode.getChildCount(); i++) {
                    AccessibilityNodeInfo accessibilityNodeInfo = rowNode.getChild(i);
                    if (accessibilityNodeInfo.isEditable()) {
                        if (accessibilityNodeInfo.isFocused()) {
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfoCompat.ACTION_PASTE);
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
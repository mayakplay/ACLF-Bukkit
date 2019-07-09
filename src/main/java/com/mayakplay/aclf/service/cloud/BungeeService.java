package com.mayakplay.aclf.service.cloud;

import com.mayakplay.aclf.dto.CloudSettings;
import org.jetbrains.annotations.Nullable;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 07.07.2019.
 */
public interface BungeeService {

    @Nullable
    CloudSettings getCloudSettings();

}

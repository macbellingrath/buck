/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.apple;

import com.facebook.buck.model.Flavor;
import com.facebook.buck.model.Flavored;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.BuildRuleType;
import com.facebook.buck.rules.Description;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

public class IosLibraryDescription implements
    Description<AppleNativeTargetDescriptionArg>, Flavored {
  public static final BuildRuleType TYPE = new BuildRuleType("ios_library");

  public static final Flavor DYNAMIC_LIBRARY = new Flavor("dynamic");

  private final Path archiver;

  public IosLibraryDescription(Path archiver) {
    this.archiver = Preconditions.checkNotNull(archiver);
  }

  @Override
  public BuildRuleType getBuildRuleType() {
    return TYPE;
  }

  @Override
  public AppleNativeTargetDescriptionArg createUnpopulatedConstructorArg() {
    return new AppleNativeTargetDescriptionArg();
  }

  @Override
  public boolean hasFlavor(Flavor flavor) {
    return flavor.equals(Flavor.DEFAULT) || flavor.equals(DYNAMIC_LIBRARY);
  }

  @Override
  public <A extends AppleNativeTargetDescriptionArg> IosLibrary createBuildRule(
      BuildRuleParams params,
      BuildRuleResolver resolver,
      A args) {
    return new IosLibrary(
        params,
        args,
        TargetSources.ofAppleSources(args.srcs),
        archiver,
        params.getBuildTarget().getFlavor().equals(DYNAMIC_LIBRARY));
  }
}

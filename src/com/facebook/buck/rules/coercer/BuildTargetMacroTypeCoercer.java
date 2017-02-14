/*
 * Copyright 2017-present Facebook, Inc.
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

package com.facebook.buck.rules.coercer;

import com.facebook.buck.io.ProjectFilesystem;
import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.rules.CellPathResolver;
import com.facebook.buck.rules.macros.BuildTargetMacro;
import com.google.common.collect.ImmutableList;

import java.nio.file.Path;

public abstract class BuildTargetMacroTypeCoercer<M extends BuildTargetMacro>
    implements MacroTypeCoercer<M> {

  private final TypeCoercer<BuildTarget> buildTargetTypeCoercer;

  public BuildTargetMacroTypeCoercer(TypeCoercer<BuildTarget> buildTargetTypeCoercer) {
    this.buildTargetTypeCoercer = buildTargetTypeCoercer;
  }

  @Override
  public boolean hasElementClass(Class<?>[] types) {
    return buildTargetTypeCoercer.hasElementClass(types);
  }

  @Override
  public void traverse(M macro, TypeCoercer.Traversal traversal) {
    buildTargetTypeCoercer.traverse(macro.getTarget(), traversal);
  }

  abstract M create(BuildTarget target);

  @Override
  public M coerce(
      CellPathResolver cellRoots,
      ProjectFilesystem filesystem,
      Path pathRelativeToProjectRoot,
      ImmutableList<String> args)
      throws CoerceFailedException {
    if (args.size() != 1) {
      throw new CoerceFailedException(
          String.format("expected exactly one argument (found %d)", args.size()));
    }
    BuildTarget target =
        buildTargetTypeCoercer.coerce(
            cellRoots,
            filesystem,
            pathRelativeToProjectRoot,
            args.get(0));
    return create(target);
  }

}
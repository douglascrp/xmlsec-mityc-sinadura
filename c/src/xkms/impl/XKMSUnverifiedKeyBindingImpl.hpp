/*
 * Copyright 2004-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * XSEC
 *
 * XKMSUnverifiedKeyBindingImpl := Implementation for UnverifiedKeyBinding
 *
 * $Id: XKMSUnverifiedKeyBindingImpl.hpp 351217 2005-02-03 14:30:30Z milan $
 *
 */

#ifndef XKMSUNVERIFIEDKEYBINDINGIMPL_INCLUDE
#define XKMSUNVERIFIEDKEYBINDINGIMPL_INCLUDE

// XSEC Includes

#include <xsec/framework/XSECDefs.hpp>
#include <xsec/xkms/XKMSUnverifiedKeyBinding.hpp>

#include "XKMSKeyBindingAbstractTypeImpl.hpp"

class XKMSUnverifiedKeyBindingImpl : public XKMSUnverifiedKeyBinding, public XKMSKeyBindingAbstractTypeImpl {

public:

	XKMSUnverifiedKeyBindingImpl(
		const XSECEnv * env
	);

	XKMSUnverifiedKeyBindingImpl(
		const XSECEnv * env, 
		XERCES_CPP_NAMESPACE_QUALIFIER DOMElement * node
	);

	virtual ~XKMSUnverifiedKeyBindingImpl() ;

	// Load
	void load(void);

	// Create
	XERCES_CPP_NAMESPACE_QUALIFIER DOMElement *
		createBlankUnverifiedKeyBinding(void);

	// Import methods from XKMSKeyBindingAbstractType
	XKMS_KEYBINDINGABSTRACTYPE_IMPL_METHODS

private:

	// Unimplemented
	XKMSUnverifiedKeyBindingImpl(void);
	XKMSUnverifiedKeyBindingImpl(const XKMSUnverifiedKeyBindingImpl &);
	XKMSUnverifiedKeyBindingImpl & operator = (const XKMSUnverifiedKeyBindingImpl &);

};

#endif /* XKMSUNVERIFIEDKEYBINDINGIMPL_INCLUDE */

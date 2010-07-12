
/*
 *   $Id$
 *
 *   Copyright 2007 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 *
 */

#include <IceUtil/UUID.h>
#include <boost_fixture.h>

void stringHandler(std::string str) {
  std::cout << "Handling:" << str << std::endl;
}

omero::model::ImagePtr new_ImageI()
{
    omero::model::ImagePtr img = new omero::model::ImageI();
    img->setAcquisitionDate(omero::rtypes::rtime(0));
    return img;
}

Fixture::Fixture()
{
  /*     log_successful_tests     = 0,
	 log_test_suites          = 1,
	 log_messages             = 2,
	 log_warnings             = 3,
	 log_all_errors           = 4, // reported by unit test macros
	 log_cpp_exception_errors = 5, // uncaught C++ exceptions
	 log_system_errors        = 6, // including timeouts, signals, traps
	 log_fatal_errors         = 7, // including unit test macros or
	 // fatal system errors
	 log_progress_only        = 8, // only unit test progress to be reported
	 log_nothing              = 9
  */

  // NOT WORKING AS IT SHOULD
  b_ut::unit_test_monitor.register_exception_translator<std::string>( &stringHandler );
  b_ut::unit_test_log.set_threshold_level( b_ut::log_messages );
  //    set_unexpected(printUnexpected);
}

Fixture::~Fixture()
{
    //    results_reporter::detailed_report();
    //    printUnexpected();
    //    show_stackframe();
}


void Fixture::show_stackframe() {
#ifdef LINUX
  void *trace[16];
  char **messages = (char **)NULL;
  int i, trace_size = 0;

  trace_size = backtrace(trace, 16);
  messages = backtrace_symbols(trace, trace_size);
  printf("[bt] Execution path:\n");
  for (i=0; i<trace_size; ++i)
	printf("[bt] %s\n", messages[i]);
#endif
}

std::string Fixture::uuid()
{
  return IceUtil::generateUUID();
}

void Fixture::printUnexpected()
{
  /* Need printStackTrace.h for this
  char* buf = new char[1024];
  printStackTrace(buf, 1024);
  std::cout << "Trace:" << buf << std::endl;
  delete[] buf;
  */
}

b_ut::test_case const & Fixture::current() {
  return b_ut::framework::current_test_case();
}


b_ut::unit_test_monitor_t& Fixture::monitor() {
  return b_ut::unit_test_monitor;
}

b_ut::unit_test_log_t& Fixture::log() {
  return b_ut::unit_test_log;
}

omero::client_ptr Fixture::login(const std::string& username, const std::string& password) {
    try {
        omero::client_ptr client = new omero::client();
        client->createSession(username, password);
        client->getSession()->closeOnDestroy();
        return client;
    } catch (const Glacier2::CannotCreateSessionException& ccse) {
        BOOST_FAIL("Threw CannotCreateSessionException:" + ccse.reason);
        return 0; // Can't reach here
    }
}

omero::client_ptr Fixture::root_login() {
    const omero::client_ptr tmp = login();
    std::string rootpass = tmp->getProperty("omero.rootpass");
    return login("root", rootpass);
}

omero::model::ExperimenterPtr Fixture::newUser(const omero::api::IAdminPrx& admin, const omero::model::ExperimenterGroupPtr& _g) {
	omero::model::ExperimenterGroupPtr g(_g);
	omero::RStringPtr name = omero::rtypes::rstring(uuid());
	long gid = -1;
	if (!g) {
		g = new omero::model::ExperimenterGroupI();
		g->setName( name );
		gid = admin->createGroup(g);
	} else {
		gid = g->getId()->getValue();
	}
	omero::model::ExperimenterPtr e = new omero::model::ExperimenterI();
	e->setOmeName( name );
	e->setFirstName( name );
	e->setLastName( name );
	long id = admin->createUser(e, name->getValue());
	return admin->getExperimenter(id);
}

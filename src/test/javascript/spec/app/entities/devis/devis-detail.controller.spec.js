'use strict';

describe('Controller Tests', function() {

    describe('Devis Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDevis, MockClient, MockMode_de_paiment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDevis = jasmine.createSpy('MockDevis');
            MockClient = jasmine.createSpy('MockClient');
            MockMode_de_paiment = jasmine.createSpy('MockMode_de_paiment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Devis': MockDevis,
                'Client': MockClient,
                'Mode_de_paiment': MockMode_de_paiment
            };
            createController = function() {
                $injector.get('$controller')("DevisDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:devisUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
